import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {UrgencyTicket} from '../../model/urgency-ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketUrgencyService {

  constructor(private http: HttpClient) {}

  // 📋 Recupera tutti i ticket
  getAllUrgencyTicket(): Observable<UrgencyTicket[]> {
    return this.http.get<UrgencyTicket[]>(ApiEndpoints.URGENCY_TICKET.GET_ALL);
  }

}
