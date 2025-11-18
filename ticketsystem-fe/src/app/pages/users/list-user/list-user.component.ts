import { Component, OnInit, Renderer2, OnDestroy } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user/user.service';
import { AuthService } from '../../../services/auth/auth.service';
import { NotificationService } from '../../../services/notification.service';
import { User } from '../../../model/user.model';
import { UserWithMenu } from '../../../model/user-with-menu.model';
import Swal from 'sweetalert2';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-list-user',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.scss']
})
export class ListUserComponent implements OnInit, OnDestroy {
  users: UserWithMenu[] = [];
  loading = false;
  error: string | null = null;
  userRoles: string[] = [];
  private clickListener?: () => void;
  private sub = new Subscription();

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private notificationService: NotificationService,
    private renderer: Renderer2,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.userRoles = this.authService.getUserRoles() || [];

    this.clickListener = this.renderer.listen('document', 'click', () => {
      this.users.forEach(u => u.showMenu = false);
    });
  }

  ngOnDestroy(): void {
    if (this.clickListener) this.clickListener();
    this.sub.unsubscribe();
  }

  loadUsers(): void {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (list) => {
        this.users = list.map(u => ({ ...u, showMenu: false }));
        this.loading = false;
      },
      error: (error: HttpErrorResponse) => {
        let message = 'Errore sconosciuto';
        if (error.status === 401) message = 'Accesso non autorizzato. Effettua il login.';
        else if (error.status === 403) message = 'Accesso negato';
        else if (error.error?.customerMessage) message = error.error.customerMessage;
        this.error = message;
        this.loading = false;
        this.notificationService.error(this.error);
      }
    });
  }

  toggleMenu(user: UserWithMenu, event: MouseEvent): void {
    event.stopPropagation();
    user.showMenu = !user.showMenu;
    this.users.forEach(u => {
      if (u !== user) u.showMenu = false;
    });
  }

  can(action: 'VIEW' | 'EDIT' | 'DELETE' | 'TOGGLE'): boolean {
    const rolePermissions: Record<string, string[]> = {
      ADMIN: ['VIEW', 'EDIT', 'DELETE', 'TOGGLE'],
      EDITOR: ['VIEW', 'EDIT', 'TOGGLE'],
      CREATOR: ['VIEW', 'EDIT'],
      FIRETOR: ['VIEW'],
      KILLER: ['VIEW', 'DELETE']
    };
    return this.userRoles.some(role => rolePermissions[role]?.includes(action));
  }

  getRoleNames(user: User): string {
    if (!user.roles?.length) return '-';
    return user.roles.map(r => r.name).join(', ');
  }

  viewUser(user: UserWithMenu) {
    const targetUrl = ['/users', user.username];
    console.log('Navigo verso:', this.router.createUrlTree(targetUrl, { queryParams: { mode: 'view' } }).toString());
    this.router.navigate(targetUrl, { queryParams: { mode: 'view' } });
  }

  editUser(user: UserWithMenu) {
    this.router.navigate(['/users', user.username], { queryParams: { mode: 'edit' } });
  }

  toggleActivation(user: UserWithMenu): void {
    const action = user.active ? 'disattivare' : 'attivare';
    Swal.fire({
      title: 'Conferma azione',
      html: `Sei sicuro di voler ${action} l'utente <b>${user.username}</b>?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Conferma',
      cancelButtonText: 'Annulla',
      confirmButtonColor: user.active ? '#d33' : '#3085d6',
      reverseButtons: true
    }).then(result => {
      if (!result.isConfirmed) return;

      const apiCall = user.active
        ? this.userService.toggleUserDeactivate(user.id!)
        : this.userService.toggleUserActive(user.id!);

      apiCall.subscribe({
        next: () => {
          this.notificationService.success(
            `Utente ${user.username} ${user.active ? 'disattivato' : 'attivato'} con successo.`
          );
          user.active = !user.active;
        },
        error: () => {
          this.notificationService.error('Errore durante aggiornamento stato utente.');
        }
      });
    });
  }

  deleteUser(user: UserWithMenu): void {
    Swal.fire({
      title: 'Sei sicuro?',
      html: `Questa azione eliminerà definitivamente l'utente <b>${user.username}</b>.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sì, elimina',
      cancelButtonText: 'Annulla',
      confirmButtonColor: '#d33',
      reverseButtons: true
    }).then(result => {
      if (!result.isConfirmed) return;
      this.userService.deleteUser(user.id!).subscribe({
        next: () => {
          this.notificationService.success(`Utente ${user.username} eliminato con successo.`);
          this.users = this.users.filter(u => u.id !== user.id);
        },
        error: () => {
          this.notificationService.error('Errore durante eliminazione utente.');
        }
      });
    });
  }
}
