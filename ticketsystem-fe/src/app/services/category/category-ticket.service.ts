import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApiEndpoints} from '../../constants/api-endpoints';
import {Category} from '../../model/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryTicketService {
  constructor(private http: HttpClient) {}

  // 📋 Recupera tutte le categorie di ticket
  getAllCategoryTickets(): Observable<Category[]> {
    return this.http.get<Category[]>(ApiEndpoints.CATEGORIES.GET_ALL);
  }

  // 📋 Recupera la categoria dall'id
  getCategoryById(id: string): Observable<Category> {
    return this.http.get<Category>(ApiEndpoints.CATEGORIES.BY_ID(id));
  }

}
