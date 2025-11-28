import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { UsersComponent } from './pages/users/users.component';
import { CreateUserComponent } from './pages/users/create-user/create-user.component';
import { UserDetailComponent } from './pages/users/user-detail/user-detail.component';
import { ListUserComponent } from './pages/users/list-user/list-user.component';
import {ListTicketComponent} from './pages/tickets/list-ticket/list-ticket.component';
import {TicketDetailComponent} from './pages/tickets/ticket-detail/ticket-detail.component';
import {CreateTicketComponent} from './pages/tickets/create-ticket/create-ticket.component';
import {CreateTicketFormComponent} from './pages/tickets/create-ticket-form/create-ticket-form.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent, data: { breadcrumb: 'Home' } },
  {
    path: 'users',
    component: UsersComponent,
    data: { breadcrumb: 'Utenti' },
    children: [
      {
        path: '',
        component: ListUserComponent,
        data: { breadcrumb: null } // non mostra "breadcrumb duplicato"
      },
      {
        path: ':username',
        component: UserDetailComponent,
        data: { breadcrumb: 'Dettaglio Utente' }
      }
    ]
  },
  { path: 'create-user', component: CreateUserComponent },
  { path: 'tickets', component: ListTicketComponent },
  { path: 'tickets/:id', component: TicketDetailComponent },
  { path: 'create-ticket', component: CreateTicketComponent },
  { path: 'ticket-create/:categoryId', component: CreateTicketFormComponent},

  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: '**', redirectTo: '/home' },
];
