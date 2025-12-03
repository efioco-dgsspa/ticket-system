import {Category} from './category.model';

export interface TicketTemplate {
  id?: string;
  category?: Category;
  name?: string;
  templateText?: string;
}
