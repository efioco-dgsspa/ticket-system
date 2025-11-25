import {Ticket} from './ticket.model';

/**
 * Estende il model Ticket aggiungendo proprietà legate alla UI (es. showMenu)
 */
export interface TicketWithMenu extends Ticket {
  showMenu?: boolean;
}
