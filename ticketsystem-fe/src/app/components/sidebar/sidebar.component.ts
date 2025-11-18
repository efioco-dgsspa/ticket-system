import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

interface MenuItem {
  label: string;
  route?: string;
  children?: MenuItem[];
  roles?: string[]; // ruoli autorizzati
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterModule, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  menuItems: MenuItem[] = [
    { label: 'Home', route: '/home' },
    { label: 'Visualizza Utenti', route: '/users', roles: ['ADMIN', 'EDITOR', 'FIRETOR', 'KILLER'] },
    { label: 'Crea Utente', route: '/create-user', roles: ['ADMIN', 'CREATOR'] },
    { label: 'Altro', route: '/', roles: ['ADMIN', 'EDITOR', 'FIRETOR', 'CREATOR', 'KILLER'] }
  ];

  filteredMenuItems: MenuItem[] = [];

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Subscribe ai cambiamenti dell'utente loggato
    this.authService.loggedIn$.subscribe(user => {
      const userRoles = user?.roles?.map(r => r.name).filter((name): name is string => !!name) || [];
      this.filteredMenuItems = this.menuItems.filter(item => {
        if (!item.roles || item.roles.length === 0) return true;
        return item.roles.some(role => userRoles.includes(role));
      });
    });
  }
}