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
export class ListTicketComponent implements OnInit{
  tickets: TicketWithMenu[] = [];
  error: string | null = null;
  page: number = 1;
  pageSize: number = 9; // quanti ticket per pagina
  userRoles: string[] = [];

  constructor(
    private router: Router,
    private ticketService : TicketService,
    private notificationService: NotificationService,
    private authService: AuthService
  ) {}

    ngOnInit(): void {
      this.loadTickets();
      this.userRoles = this.authService.getUserRoles() || [];
    }

  loadTickets(): void {
    this.ticketService.getAllTickets().subscribe({
  next: (list) => {
    this.tickets = list;
    console.log(this.tickets);
  },
  error: (error: HttpErrorResponse) => {
    let message = 'Errore sconosciuto';
    if (error.status === 401) message = 'Accesso non autorizzato. Effettua il login.';
    else if (error.status === 403) message = 'Accesso negato';
    else if (error.error?.customerMessage) message = error.error.customerMessage;
    this.error = message;
    this.notificationService.error(this.error);
  }
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
    console.log('Navigo verso:', this.router.createUrlTree(targetUrl, { queryParams: { mode: 'view' } }).toString());
    this.router.navigate(targetUrl, { queryParams: { mode: 'view' } });
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
}
