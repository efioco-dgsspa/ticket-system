import {Ticket} from './ticket.model';

export interface TicketResponse {
  ticket?: Ticket;
  tickets?: Ticket[];
  message?: string;
}
