import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ApiEndpoints } from "../../constants/api-endpoints";
import { Role } from "../../model/role.model"; // Assicurati di avere questo model
import { RoleResponse } from "../../model/role.response.model"; // Se hai un wrapper come UserResponse

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  constructor(private http: HttpClient) {}

  // 📋 Recupera tutti i ruoli
  getAllRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(ApiEndpoints.ROLES.GET_ALL);
  }
}
