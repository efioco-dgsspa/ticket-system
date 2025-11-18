import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AuthResponse } from '../../model/auth-response.model';
import { AuthRequest } from '../../model/auth-request.model';
import { ApiEndpoints } from '../../constants/api-endpoints';
import { AuthError } from '../../model/auth-error';
import { User } from '../../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly tokenKey = 'access_token';
  private readonly refreshKey = 'refresh_token';

  private loggedIn = new BehaviorSubject<User | null>(this.loadUserFromStorage());
  loggedIn$ = this.loggedIn.asObservable();

  constructor(private http: HttpClient) {}

  /** 🔑 Effettua login */
  login(credentials: AuthRequest): Observable<AuthResponse> {
    console.log('[AuthService] 📤 Tentativo di login...');
    return this.http.post<AuthResponse>(ApiEndpoints.AUTH.LOGIN, credentials).pipe(
      tap(response => {
        console.log('[AuthService] ✅ Login riuscito, risposta ricevuta:', response);
        
        if (response.accessToken && response.refreshToken) {
          this.storeTokens(response.accessToken, response.refreshToken);
          console.log('[AuthService] 💾 Token salvati nel localStorage');
        }
        
        if (response.user) {
          this.setUser(response.user);
          console.log('[AuthService] 👤 Utente salvato:', response.user);
        }
      }),
      catchError(this.handleError)
    );
  }

  /** ♻️ Refresh token */
  refreshToken(): Observable<{ accessToken: string; refreshToken?: string }> {
    const refreshToken = localStorage.getItem(this.refreshKey);
    
    if (!refreshToken) {
      throw new Error('Nessun refresh token disponibile');
    }

    return this.http
      .post<{ accessToken: string; refreshToken?: string }>(
        ApiEndpoints.AUTH.REFRESH,
        { refreshToken }
      )
      .pipe(
        tap(response => {
          // Salva il nuovo access token
          localStorage.setItem(this.tokenKey, response.accessToken);
          
          // Se il backend restituisce anche un nuovo refresh token, salvalo
          if (response.refreshToken) {
            localStorage.setItem(this.refreshKey, response.refreshToken);
          }
        })
      );
  }

  /** 🔒 Logout */
  logout(): void {
    console.log('[AuthService] 🚪 Logout eseguito → rimozione token e utente');
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.refreshKey);
    localStorage.removeItem('user');
    this.loggedIn.next(null);
  }

  /** ✅ Verifica se esiste un access token valido */
  hasValidAccessToken(): boolean {
    return !!this.getAccessToken();
  }

  /** 🧠 Getter/Setter token */
  getAccessToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshKey);
  }

  public saveAccessToken(token: string): void {
    console.log('[AuthService] 💾 Salvo nuovo token:', token.substring(0, 20) + '...');
    localStorage.setItem(this.tokenKey, token);
    console.log('[AuthService] ✅ Token salvato, verifico:', localStorage.getItem(this.tokenKey)?.substring(0, 20) + '...');
  }

  private storeTokens(accessToken: string, refreshToken: string): void {
    localStorage.setItem(this.tokenKey, accessToken);
    localStorage.setItem(this.refreshKey, refreshToken);
  }

  /** 👤 Gestione utente loggato */
  getUser(): User | null {
    return this.loggedIn.value;
  }

  private loadUserFromStorage(): User | null {
    const raw = localStorage.getItem('user');
    return raw ? (JSON.parse(raw) as User) : null;
  }

  setUser(user: User): void {
    this.loggedIn.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  /** Restituisce i ruoli dell'utente loggato come array di stringhe */
  getUserRoles(): string[] {
    const user = this.getUser();
    if (!user || !user.roles) return [];
    return user.roles
      .map(r => r.name)       // estrai il name
      .filter((name): name is string => !!name); // rimuovi eventuali undefined/null
  }

  /** ⚠️ Gestione errori */
  private handleError(error: HttpErrorResponse) {
    let message = 'Errore sconosciuto';
    if (error.status === 401) message = 'Credenziali non valide';
    else if (error.status === 403) message = 'Accesso negato';
    else if (error.error?.customerMessage) message = error.error.customerMessage;

    console.log("stampo errore message: ", message)
    console.error('[AuthService] ❌ handleError:', message, error);
    return throwError(() => new AuthError(message));
  }
}