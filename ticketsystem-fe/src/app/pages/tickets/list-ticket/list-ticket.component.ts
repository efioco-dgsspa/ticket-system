import {Component, OnInit, Renderer2} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Router, RouterModule} from '@angular/router';
import {NotificationService} from '../../../services/notification.service';
import {HttpErrorResponse} from '@angular/common/http';
import {TicketService} from '../../../services/ticket/ticket.service';
import {Ticket} from '../../../model/ticket.model';
import {UserWithMenu} from '../../../model/user-with-menu.model';
import {TicketWithMenu} from '../../../model/ticket-with-menu.model';
import {AuthService} from '../../../services/auth/auth.service';
import {TicketStatusService} from '../../../services/ticket-status/ticket-status.service';
import {CategoryTicketService} from '../../../services/category/category-ticket.service';
import {TicketUrgencyService} from '../../../services/ticket-urgency/ticket-urgency.service';
import {UserService} from '../../../services/user/user.service';

@Component({
  selector: 'app-list-ticket',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterModule,
    ReactiveFormsModule
  ],
  templateUrl: './list-ticket.component.html',
  styleUrl: './list-ticket.component.scss',
})
export class ListTicketComponent implements OnInit {
  tickets: TicketWithMenu[] = [];
  error: string | null = null;
  page: number = 1;
  pageSize: number = 4; // quanti ticket per pagina
  userRoles: string[] = [];
  stati: any[] = [];
  categorie: any[] = [];
  urgenze: any[] = [];
  listUserOperator: any[] = [];

  search = {
    customerId: '',
    title: '',
    status: '',
    assignedTo: '',
    category: '',
    createdAt: '',
    urgency: ''
  };

  constructor(
    private router: Router,
    private ticketService: TicketService,
    private notificationService: NotificationService,
    private authService: AuthService,
    private ticketStatusService: TicketStatusService,
    private categoryTicketService: CategoryTicketService,
    private ticketUrgencyService: TicketUrgencyService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadTickets();
    this.userRoles = this.authService.getUserRoles() || [];
    this.loadStatuses();
    this.loadCategories();
    this.loadUrgencies();
    this.loadUsersByRole("OPERATOR");
  }

  private handleHttpError(error: HttpErrorResponse): void {
    let message = 'Errore sconosciuto';

    if (error.status === 401) message = 'Accesso non autorizzato. Effettua il login.';
    else if (error.status === 403) message = 'Accesso negato';
    else if (error.error?.customerMessage) message = error.error.customerMessage;

    this.error = message;
    this.notificationService.error(message);
  }

  loadTickets(): void {
    this.ticketService.getAllTickets().subscribe({
      next: (list) => {
        this.tickets = list;
      },
      error: error => this.handleHttpError(error)
    });
  }

  loadUsersByRole(role: string): void {
    this.userService.getUsersByRoleName(role).subscribe({
      next: (list) => {
        this.listUserOperator = list;
      },
      error: error => this.handleHttpError(error)
    });
  }

  loadStatuses(): void {
    this.ticketStatusService.getAllStatus().subscribe({
      next: (list) => {
        this.stati = list;
      },
      error: error => this.handleHttpError(error)
    });
  }

  loadCategories(): void {
    this.categoryTicketService.getAllCategoryTickets().subscribe({
      next: (list) => {
        this.categorie = list;
      },
      error: error => this.handleHttpError(error)
    });
  }

  loadUrgencies(): void {
    this.ticketUrgencyService.getAllUrgencyTicket().subscribe({
      next: (list) => {
        this.urgenze = list;
      },
      error: error => this.handleHttpError(error)
    });
  }

  get paginatedTickets(): TicketWithMenu[] {
    const start = (this.page - 1) * this.pageSize;
    return this.tickets.slice(start, start + this.pageSize);
  }

  nextPage() {
    if ((this.page * this.pageSize) < this.tickets.length) {
      this.page++;
    }
  }

  prevPage() {
    if (this.page > 1) {
      this.page--;
    }
  }

  protected readonly Math = Math;

  toggleMenu(ticket: TicketWithMenu, event: MouseEvent): void {
    event.stopPropagation();
    ticket.showMenu = !ticket.showMenu;
    this.tickets.forEach(t => {
      if (t !== ticket) t.showMenu = false;
    });
  }

  viewTicket(ticket: TicketWithMenu) {
    const targetUrl = ['/tickets', ticket.id];
    console.log('Navigo verso:', this.router.createUrlTree(targetUrl, {queryParams: {mode: 'view'}}).toString());
    this.router.navigate(targetUrl, {queryParams: {mode: 'view'}});
  }

  can(action: 'VIEW' | 'EDIT' | 'DELETE' | 'TOGGLE'): boolean {
    const rolePermissions: Record<string, string[]> = {
      ADMIN: ['VIEW', 'EDIT', 'DELETE'],
      EDITOR: ['VIEW', 'EDIT'],
      CREATOR: ['VIEW', 'EDIT'],
      FIRETOR: ['VIEW'],
      KILLER: ['VIEW', 'DELETE']
    };
    return this.userRoles.some(role => rolePermissions[role]?.includes(action));
  }

  resetSearch(): void {
    this.search = {customerId: '', title: '', status: '', assignedTo: '', category: '', createdAt: '', urgency: ''};
    this.loadTickets();
  }

  applySearch(): void {
    const request = {
      page: 0,
      size: 20,
      ticket: {
        customerId: this.search.customerId || null,
        title: this.search.title || null,
        status: this.search.status ? { id: this.search.status } : null,
        assignedTo: this.search.assignedTo ? { id: this.search.assignedTo } : null,
        category: this.search.category ? { id: this.search.category } : null,
        urgency: this.search.urgency ? { id: this.search.urgency } : null,
        createdAt: this.search.createdAt
          ? this.search.createdAt + 'T00:00:00'
          : null
      }
    };
    console.log(request)
    this.ticketService.searchTicket(request).subscribe({
      next: (list) => {
        this.tickets = list;
      },
      error: () => {
        this.notificationService.error('Errore durante la ricerca filtrata dei ticket.');
      }
    });
  }
}
