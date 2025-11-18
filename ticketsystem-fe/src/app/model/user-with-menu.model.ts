import { User } from './user.model';

/**
 * Estende il model User aggiungendo proprietà legate alla UI (es. showMenu)
 */
export interface UserWithMenu extends User {
  showMenu?: boolean;
}
