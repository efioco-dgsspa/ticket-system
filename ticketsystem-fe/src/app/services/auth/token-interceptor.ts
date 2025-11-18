import { HttpInterceptorFn, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, catchError, filter, switchMap, take, throwError, of, delay } from 'rxjs';
import { AuthService } from './auth.service';
import { jwtDecode } from 'jwt-decode';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

// Funzione di utilità per capire se un token è scaduto
function isTokenExpired(token: string | null): boolean {
  if (!token) return true;
  try {
    const { exp } = jwtDecode<{ exp: number }>(token);
    const isExpired = Date.now() > exp * 1000;
    console.log('[TokenInterceptor] Token scaduto?', isExpired, 'Scadenza:', new Date(exp * 1000));
    return isExpired;
  } catch {
    console.warn('[TokenInterceptor] Impossibile decodificare il token');
    return true;
  }
}

// Aggiunge l'header Authorization
function addTokenHeader(req: HttpRequest<any>, token: string) {
  return req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });
}

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // Escludi login e refresh
  if (req.url.includes('/auth/login') || req.url.includes('/auth/refresh')) {
    return next(req);
  }

  const token = authService.getAccessToken();
  console.log('[TokenInterceptor] Richiesta verso', req.url);
  console.log('[TokenInterceptor] Header Authorization attuale:', token ? `Bearer ${token.substring(0, 20)}...` : 'nessuno');

  // Se token esiste e non è scaduto → aggiungi
  if (token && !isTokenExpired(token)) {
    req = addTokenHeader(req, token);
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        console.log('[TokenInterceptor] 401 ricevuto per:', req.url);

        if (isRefreshing) {
          // Se un refresh è in corso, metti la richiesta in coda
          return refreshTokenSubject.pipe(
            filter(t => t !== null),
            take(1),
            switchMap(newToken => next(addTokenHeader(req, newToken!)))
          );
        }

        isRefreshing = true;
        refreshTokenSubject.next(null);

        // Chiama refreshToken
        return authService.refreshToken().pipe(
          switchMap(response => {
            const newToken = response.accessToken;
            if (!newToken) {
              throw new Error('Refresh fallito: nessun access token ricevuto');
            }

            authService.saveAccessToken(newToken);
            isRefreshing = false;
            refreshTokenSubject.next(newToken);

            console.log('[TokenInterceptor] 🔄 Riprovo richiesta originale con nuovo token');
            return next(addTokenHeader(req, newToken));
          }),
          catchError(err => {
            isRefreshing = false;
            refreshTokenSubject.next(null);
            console.error('[TokenInterceptor] ❌ Errore refresh token:', err);
            authService.logout();
            return throwError(() => err);
          })
        );
      }

      return throwError(() => error);
    })
  );
};
