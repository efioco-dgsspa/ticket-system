import { environment } from "../../environments/environment";

export class ApiEndpoints {
  private static readonly BASE_URL = environment.apiBaseUrl;

  // 🔐 AUTH ENDPOINTS
  static readonly AUTH = {
    LOGIN: `${this.BASE_URL}/auth/login`,
    REFRESH: `${this.BASE_URL}/auth/refresh`,
  };

  // 👤 USER ENDPOINTS
  static readonly USERS = {
    BASE: `${this.BASE_URL}/users`,
    CREATE: `${this.BASE_URL}/users`,
    GET_ALL: `${this.BASE_URL}/users`,
    BY_USERNAME: (username: string) => `${this.BASE_URL}/users/by-username/${username}`,
    BY_EMAIL: (email: string) => `${this.BASE_URL}/users/by-email/${email}`,
    UPDATE: `${this.BASE_URL}/users/update-user`,
    ACTIVATE: (id: string) => `${this.BASE_URL}/users/${id}/activate`,
    DEACTIVATE: (id: string) => `${this.BASE_URL}/users/${id}/deactivate`,
    DELETE: (id: string) => `${this.BASE_URL}/users/${id}`,
  };

  // 👑 ROLE ENDPOINTS
  static readonly ROLES = {
    BASE: `${this.BASE_URL}/roles`,
    GET_ALL: `${this.BASE_URL}/roles`,
  };

  // 🗂️ CATEGORY ENDPOINTS
  static readonly CATEGORIES = {
    BASE: `${this.BASE_URL}/categories`,
    GET_ALL: `${this.BASE_URL}/categories`,
  };
}