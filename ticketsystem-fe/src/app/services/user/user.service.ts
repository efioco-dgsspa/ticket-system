import { Injectable } from "@angular/core";
import { UserRequest } from "../../model/user.request.model";
import { UserResponse } from "../../model/user.response.model";
import { catchError, Observable, tap, throwError } from "rxjs";
import { ApiEndpoints } from "../../constants/api-endpoints";
import { User } from "../../model/user.model";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient) {}

  // ➕ Crea un nuovo utente
  createUser(request: UserRequest): Observable<UserResponse> {
    console.log('[UserService] HttpClient:', this.http.constructor.name);
    return this.http.post<UserResponse>(ApiEndpoints.USERS.CREATE, request).pipe(
      tap(() => console.log('[UserService] ✅ User creato con successo')),
      catchError(err => {
        console.error('[UserService] ❌ Errore creazione user:', err);
        return throwError(() => err);
      })
    );
  }

  // 📋 Recupera tutti gli utenti
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(ApiEndpoints.USERS.GET_ALL);
  }

  // 🔍 Recupera gli utenti sulla base dei filtri
  searchUsers(request: any) {
    return this.http.post<User[]>(ApiEndpoints.USERS.SEARCH, request);
  }

  // 🔍 Cerca per username
  getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(ApiEndpoints.USERS.BY_USERNAME(username));
  }

  // 🔍 Cerca per email
  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(ApiEndpoints.USERS.BY_EMAIL(email));
  }

  // ✏️ Aggiorna utente
  updateUser(request: UserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(ApiEndpoints.USERS.UPDATE, request);
  }

  // 🔄 Attiva utente
  toggleUserActive(id: string): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      ApiEndpoints.USERS.ACTIVATE(id), 
      {}, // ← body vuoto o dati se necessari
      { responseType: 'json' } // ← opzionale);
    ); 
  }

  // 🔄 Disattiva utente
  toggleUserDeactivate(id: string): Observable<UserResponse> {
    return this.http.patch<UserResponse>(
      ApiEndpoints.USERS.DEACTIVATE(id), 
      {}, // ← body vuoto o dati se necessari
      { responseType: 'json' } // ← opzionale);
    ); 
  }

  // ❌ Elimina utente
  deleteUser(id: string): Observable<string> {
    return this.http.delete<string>(ApiEndpoints.USERS.DELETE(id));
  }
}
