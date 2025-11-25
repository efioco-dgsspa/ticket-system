import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {Ticket} from '../../model/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  constructor(private http: HttpClient) {}


  // 📋 Recupera tutti i ticket
  getAllTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(ApiEndpoints.TICKETS.GET_ALL);
  }



}
