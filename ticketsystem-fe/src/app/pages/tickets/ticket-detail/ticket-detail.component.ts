import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {NotificationService} from '../../../services/notification.service';
import {TicketService} from '../../../services/ticket/ticket.service';
import {TicketWithMenu} from '../../../model/ticket-with-menu.model';
import {Ticket} from '../../../model/ticket.model';
import {DatePipe, NgFor, NgIf} from '@angular/common';

@Component({
  selector: 'app-ticket-detail',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    DatePipe,
    NgFor,
    NgIf
  ],
  templateUrl: './ticket-detail.component.html',
  styleUrl: './ticket-detail.component.scss',
})
export class TicketDetailComponent implements OnInit{
  ticketForm!: FormGroup;

  ticket!: Ticket;

  constructor(
    private notificationService: NotificationService,
    private route: ActivatedRoute,
    private ticketService: TicketService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.formInitialization();

    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.notificationService.error('Parametro id mancante');
      return;
    }

    this.ticketService.getTicketById(id).subscribe({
      next: (ticket) => {
        this.ticket = ticket;
        this.buildForm(ticket);
      },
      error: () => {
        this.notificationService.error('Impossibile caricare utente');
      }
    });
  }


  buildForm(ticket: Ticket) {
    console.log(ticket)

    this.ticketForm = this.fb.group({
      customer: [{ value: ticket.customerId, disabled: true }],
      title: [{ value: ticket.title, disabled: true }],
      description: [{ value: ticket.description, disabled: true }],
      status: [{ value: ticket.status?.name, disabled: true }],
      category: [{ value: ticket.category?.name, disabled: true }],
      creator: [{ value: ticket.creator?.username, disabled: true }],
      createdAt: [{ value: ticket.createdAt, disabled: true }],
      updatedAt: [{ value: ticket.updatedAt, disabled: true }],
      assignedToId: [{ value: ticket.assignedTo?.username, disabled: true }],
      //ticketMessages: [{ value: ticket.messages, disabled: true }],
      urgency: [{ value: ticket.urgency?.description, disabled: true }]
    });
  }

  formInitialization(){
    this.ticketForm = this.fb.group({
      customer: [''],
      title: [''],
      description: [''],
      status: [''],
      category: [''],
      creator: [''],
      createdAt: [''],
      updatedAt: [''],
      assignedToId: [''],
      ticketMessages: [''],
      urgency: ['']
    });
  }

  goBack(): void {
    this.router.navigate(['/tickets']);
  }
}
