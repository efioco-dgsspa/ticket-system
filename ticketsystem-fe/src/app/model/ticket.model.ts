import {Category} from './category.model';
import {Status} from './status-ticket.model';
import {UrgencyTicket} from './urgency-ticket.model';
import {User} from './user.model';

export interface Ticket {
  id?: string;
  customerId?: string;
  title?: string;
  description?: string;
  status?: Status;
  category?: Category;
  creatorId?: string;
  createdAt?: string;
  updatedAt?: string;
  removed?: boolean;
  assignedToId?: string;
  ticketMessages?: string;
  urgency?: UrgencyTicket;
}
