import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {Status} from '../../model/status-ticket.model';

@Injectable({
  providedIn: 'root'
})
export class TicketStatusService {

  constructor(private http: HttpClient) {}

  // 📋 Recupera tutti gli stati
  getAllStatus(): Observable<Status[]> {
    return this.http.get<Status[]>(ApiEndpoints.STATUS_TICKET.GET_ALL);
  }

}
