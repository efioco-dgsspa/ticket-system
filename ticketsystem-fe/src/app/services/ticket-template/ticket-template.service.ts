import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {TicketTemplate} from '../../model/ticket-template.model';

@Injectable({
  providedIn: 'root'
})
export class TicketTemplateService {
  constructor(private http: HttpClient) {}

  // 📋 Recupera le sottocategorie dall'id categoria
  getTicketTemplateByIdCategory(id: string): Observable<TicketTemplate[]> {
    return this.http.get<TicketTemplate[]>(ApiEndpoints.TICKET_TEMPLATE.BY_ID_CATEGORY(id));
  }

}
