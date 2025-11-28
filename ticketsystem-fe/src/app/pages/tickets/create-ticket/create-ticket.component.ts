import {Component, OnInit} from '@angular/core';
import {CommonModule, NgIf} from '@angular/common';
import {Category} from '../../../model/category.model';
import {HttpErrorResponse} from '@angular/common/http';
import {NotificationService} from '../../../services/notification.service';
import {CategoryTicketService} from '../../../services/category/category-ticket.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-ticker',
  imports: [
    NgIf,
    CommonModule
  ],
  templateUrl: './create-ticket.component.html',
  styleUrl: './create-ticket.component.scss',
})
export class CreateTicketComponent implements OnInit{
  error: string | null = null;
  categories: Category[] = [];

  constructor(
    private categoryService : CategoryTicketService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
      this.loadCategory();
  }

  onCategoryClick(cat: Category) {
    console.log('Categoria cliccata =', cat);
    this.router.navigate(['/ticket-create', cat.id]);
  }

  loadCategory(): void {
    this.categoryService.getAllCategoryTickets().subscribe({
      next: (list) => {
        this.categories = list;
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

}
