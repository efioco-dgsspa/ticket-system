import { environment } from "../../environments/environment";

export class ApiEndpoints {
  // Base URL principale
  private static get BASE_URL(): string {
    return environment.apiBaseUrl;
  }

  // Sotto-path già incapsulati
  private static get AUTH_BASE_URL(): string {
    return `${this.BASE_URL}/auth`;
  }

  private static get USER_BASE_URL(): string {
    return `${this.BASE_URL}/users`;
  }

  private static get ROLE_BASE_URL(): string {
    return `${this.BASE_URL}/roles`;
  }

  private static get CATEGORY_BASE_URL(): string {
    return `${this.BASE_URL}/categories`;
  }

  private static get TICKET_BASE_URL(): string {
    return `${this.BASE_URL}/tickets`;
  }

  private static get TICKET_TEMPLATE_BASE_URL(): string {
    return `${this.BASE_URL}/ticket-templates`;
  }

  private static get URGENCY_TICKET_BASE_URL(): string {
    return `${this.BASE_URL}/ticket-urgencies`;
  }

  private static get STATUS_TICKET_BASE_URL(): string {
    return `${this.BASE_URL}/statuses`;
  }

  // 🔐 AUTH ENDPOINTS
  static readonly AUTH = {
    LOGIN: `${ApiEndpoints.AUTH_BASE_URL}/login`,
    REFRESH: `${ApiEndpoints.AUTH_BASE_URL}/refresh`,
  };

  // 👤 USER ENDPOINTS
  static readonly USERS = {
    BASE: ApiEndpoints.USER_BASE_URL,
    CREATE: ApiEndpoints.USER_BASE_URL,
    GET_ALL: ApiEndpoints.USER_BASE_URL,
    SEARCH: `${ApiEndpoints.USER_BASE_URL}/search-users`,
    BY_USERNAME: (username: string) =>
      `${ApiEndpoints.USER_BASE_URL}/by-username/${username}`,
    BY_EMAIL: (email: string) =>
      `${ApiEndpoints.USER_BASE_URL}/by-email/${email}`,
    UPDATE: `${ApiEndpoints.USER_BASE_URL}/update-user`,
    ACTIVATE: (id: string) =>
      `${ApiEndpoints.USER_BASE_URL}/${id}/activate`,
    DEACTIVATE: (id: string) =>
      `${ApiEndpoints.USER_BASE_URL}/${id}/deactivate`,
    DELETE: (id: string) =>
      `${ApiEndpoints.USER_BASE_URL}/${id}`,
  };

  // 👑 ROLE ENDPOINTS
  static readonly ROLES = {
    BASE: ApiEndpoints.ROLE_BASE_URL,
    GET_ALL: ApiEndpoints.ROLE_BASE_URL,
  };

  // 🗂️ CATEGORY ENDPOINTS
  static readonly CATEGORIES = {
    BASE: ApiEndpoints.CATEGORY_BASE_URL,
    GET_ALL: ApiEndpoints.CATEGORY_BASE_URL,
    SEARCH: `${ApiEndpoints.CATEGORY_BASE_URL}/search-categories`,
    BY_ID: (id: string) =>
      `${ApiEndpoints.CATEGORY_BASE_URL}/by-id/${id}`
  };

  // 🗂️ TICKET ENDPOINTS
  static readonly TICKETS = {
    BASE: ApiEndpoints.TICKET_BASE_URL,
    GET_ALL: ApiEndpoints.TICKET_BASE_URL,
    CREATE: ApiEndpoints.TICKET_BASE_URL,
    BY_ID: (id: string) =>
      `${ApiEndpoints.TICKET_BASE_URL}/by-id/${id}`,
    SEARCH: `${ApiEndpoints.TICKET_BASE_URL}/search-tickets`,
  };

  // 🗂️ URGENCY TICKET ENDPOINTS
  static readonly URGENCY_TICKET = {
    BASE: ApiEndpoints.URGENCY_TICKET_BASE_URL,
    GET_ALL: ApiEndpoints.URGENCY_TICKET_BASE_URL,
  };

  // 🗂️ TICKET TEMPLATE ENDPOINTS
  static readonly TICKET_TEMPLATE = {
    BASE: ApiEndpoints.TICKET_TEMPLATE_BASE_URL,
    GET_ALL: ApiEndpoints.TICKET_TEMPLATE_BASE_URL,
    BY_ID_CATEGORY: (id: string) =>
      `${ApiEndpoints.TICKET_TEMPLATE_BASE_URL}/by-id-category/${id}`
  };

  // 🗂️ TICKET STATUS ENDPOINTS
  static readonly STATUS_TICKET  = {
    BASE: ApiEndpoints.STATUS_TICKET_BASE_URL,
    GET_ALL: ApiEndpoints.STATUS_TICKET_BASE_URL
  };
}
