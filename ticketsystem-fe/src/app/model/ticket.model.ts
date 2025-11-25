import {Category} from './category.model';
import {Status} from './status-ticket.model';

export interface Ticket {
  id?: string;
  customerId?: string;
  title?: string;
  descrizione?: string;
  status?: Status;
  category?: Category;
  creatorID?: string;
  createdAt?: string;
  updatedAt?: string;
  removed?: boolean;
  assignedToId?: string;
  ticketMessages?: string;
}
