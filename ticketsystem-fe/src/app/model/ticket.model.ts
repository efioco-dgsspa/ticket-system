import {Category} from './category.model';
import {Status} from './status-ticket.model';
import {UrgencyTicket} from './urgency-ticket.model';
import {User} from './user.model';
import {TicketTemplate} from './ticket-template.model';
import {TicketMessage} from './ticket-message.model';

export interface Ticket {
  id?: string;
  customerId?: string;
  title?: string;
  description?: string;
  status?: Status;
  category?: Category;
  creatorId?: string;
  creator?: User;
  createdAt?: string;
  updatedAt?: string;
  removed?: boolean;
  assignedToId?: string;
  messages?: TicketMessage[];
  urgency?: UrgencyTicket;
  sottocategorie?: TicketTemplate;
  firstMessage?: string;
}
