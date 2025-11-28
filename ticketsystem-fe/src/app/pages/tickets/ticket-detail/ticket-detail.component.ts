import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';
import {NotificationService} from '../../../services/notification.service';
import {TicketService} from '../../../services/ticket/ticket.service';
import {TicketWithMenu} from '../../../model/ticket-with-menu.model';
import {Ticket} from '../../../model/ticket.model';

@Component({
  selector: 'app-ticket-detail',
  imports: [
    FormsModule,
    ReactiveFormsModule
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
  ) {}

    ngOnInit(): void {

      const id = this.route.snapshot.paramMap.get('id');
      if (!id) {
        this.notificationService.error('Parametro id mancante');
        return;
      }

/* this.ticketService.getTicketById(id).subscribe({
  next: (ticket) => {
    this.ticket = ticket;
    this.buildForm(ticket);
    this.loading = false;
  },
  error: () => {
    this.notificationService.error('Impossibile caricare utente');
    this.loading = false;
  }
}); */

      // MOCK DEL TICKET (simula la risposta del backend)
      const mockTicket: TicketWithMenu = {
        id: id,
        customerId: 'C001',
        title: 'Problema login',
        description: 'Non riesco a fare login',
        // status: 'OPEN',
        // categoryID: 'TECH',
        //creatorID: new User(),
        createdAt: new Date().toLocaleString(),
        updatedAt: new Date().toLocaleString(),
        ticketMessages: 'Messaggio iniziale',
        assignedToId: 'ciao'
      };

      this.ticket = mockTicket;
      this.buildForm(mockTicket);
}


  buildForm(ticket: Ticket) {
    this.ticketForm = this.fb.group({
      customer: [{ value: ticket.customerId, disabled: true }],
      title: [{ value: ticket.title, disabled: true }],
      descrizione: [{ value: ticket.description, disabled: true }],
      //status: [{ value: ticket.statusID, disabled: true }],
      //category: [{ value: ticket.categoryID, disabled: true }],
      creatorID: [{ value: ticket.creatorId, disabled: true }],
      createdAt: [{ value: ticket.createdAt, disabled: true }],
      updatedAt: [{ value: ticket.updatedAt, disabled: true }],
      assignedToId: [{ value: ticket.assignedToId, disabled: true }],
      ticketMessages: [{ value: ticket.ticketMessages, disabled: true }]
    });
  }

}
