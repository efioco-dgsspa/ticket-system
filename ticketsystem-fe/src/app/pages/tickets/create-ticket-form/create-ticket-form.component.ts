import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {CategoryTicketService} from '../../../services/category/category-ticket.service';
import {Category} from '../../../model/category.model';
import {User} from '../../../model/user.model';
import {AuthService} from '../../../services/auth/auth.service';
import {TicketUrgencyService} from '../../../services/ticket-urgency/ticket-urgency.service';
import Swal from 'sweetalert2';
import {TicketRequest} from '../../../model/ticket.request.model';
import {TicketService} from '../../../services/ticket/ticket.service';

@Component({
  selector: 'app-create-ticket-form',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './create-ticket-form.component.html',
  styleUrl: './create-ticket-form.component.scss',
})
export class CreateTicketFormComponent implements OnInit {
  categoryId: string | null = null;
  ticketForm!: FormGroup;
  categorySelected: Category | undefined;
  utenteInSessione: User | null = null;
  urgencies: any[] = [];

  constructor(private route: ActivatedRoute,
              private fb: FormBuilder,
              private categoryService: CategoryTicketService,
              private authService: AuthService,
              private ticketUrgencyService: TicketUrgencyService,
              private router: Router,
              private ticketService: TicketService) {
  }

  ngOnInit(): void {
    this.categoryId = this.route.snapshot.paramMap.get('categoryId');
    this.utenteInSessione = this.authService.getUser();
    this.initForm();
    this.loadUrgency();
    this.loadUserSession();

    if (this.categoryId) {
      this.loadCategory(this.categoryId);
    } else {
      console.error("categoryId è null!");
    }
  }

  private initForm(): void {
    this.ticketForm = this.fb.group({
      categoria: ['', [Validators.required]],
      titolo: ['', [Validators.required, Validators.minLength(3)]],
      descrizione: ['', [Validators.required, Validators.minLength(3)]],
      creator: ['', [Validators.required]],
      urgency: [null, Validators.required]
    });
  }

  private loadCategory(id: string): void {
    this.categoryService.getCategoryById(id).subscribe({
      next: (categories) => {
        this.categorySelected = categories;

        // Imposto il valore della categoria nel form
        this.ticketForm.patchValue({
          categoria: this.categorySelected.name
        });
        this.ticketForm.get('categoria')?.disable();
      },
      error: (err) => {
        console.error('Errore caricamento categoria:', err);
      }
    });
  }

  private loadUrgency(): void {
    this.ticketUrgencyService.getAllUrgencyTicket().subscribe({
      next: (listUrgency) => {
        this.urgencies = listUrgency;
      },
      error: (err) => {
        console.error('Errore caricamento urgenze:', err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/create-ticket']);
  }

  private loadUserSession(): void {
    // Imposto il valore dell'utente nel form
    this.ticketForm.patchValue({
      creator: this.utenteInSessione?.username
    });
    this.ticketForm.get('creator')?.disable();
  }

  onSubmit(): void {
    if (this.ticketForm.invalid) {
      this.ticketForm.markAllAsTouched();
      return;
    }

    const request: TicketRequest = {
      ticket: {
        title: this.ticketForm.value.titolo,
        description: this.ticketForm.value.descrizione,
        creatorId: this.utenteInSessione!.id,
        urgency: this.urgencies.find(u => u.id === this.ticketForm.value.urgency),
        category: this.categorySelected
      }
    };

    console.log('[UserService] 🔍 Prima di creare ticket, token presente?', !!this.authService.getAccessToken());
    console.log('[UserService] 🔍 Refresh token presente?', !!this.authService.getRefreshToken());

    this.ticketService.createTicket(request).subscribe({
      next: () => {
        Swal.fire('Successo!', 'Ticket creato correttamente', 'success');
        this.ticketForm.reset();
      },
      error: (err) => {
        console.error('Errore creazione ticket:', err);
        Swal.fire('Errore',
          err.error?.customerMessage ? err.error?.customerMessage : 'Si è verificato un errore durante la creazione del ticket'
        );
      }
    });
  }
}
