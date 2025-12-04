import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {Ticket} from '../../model/ticket.model';
import {TicketRequest} from '../../model/ticket.request.model';
import {TicketResponse} from '../../model/ticket.response.model';
import {catchError, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor(private http: HttpClient) {}


  // 📋 Recupera tutti i ticket
  getAllTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(ApiEndpoints.TICKETS.GET_ALL);
  }

  // ➕ Crea un nuovo ticket
  createTicket(request: TicketRequest): Observable<TicketResponse> {
    console.log('[TicketService] HttpClient:', this.http.constructor.name);
    console.log(request);
    return this.http.post<TicketResponse>(ApiEndpoints.TICKETS.CREATE, request).pipe(
      tap(() => console.log('[TicketService] ✅ Ticket creato con successo')),
      catchError(err => {
        console.error('[TicketService] ❌ Errore creazione ticket:', err);
        return throwError(() => err);
      })
    );
  }

  // 📋 Recupera il ticket dall'id
  getTicketById(id: string): Observable<Ticket> {
    return this.http.get<Ticket>(ApiEndpoints.TICKETS.BY_ID(id));
  }

  // 🔍 Recupera i ticket sulla base dei filtri
  searchTicket(request: any) {
    return this.http.post<Ticket[]>(ApiEndpoints.TICKETS.SEARCH, request);
  }

}
