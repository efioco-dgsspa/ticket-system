import { Role } from "./role.model";

export interface User {
  id?: string;
  username?: string;
  password?: string;
  email?: string;
  roles?: Role[];
  active?: boolean;
  passwordCorrente?: string;
  nuovaPassword?: string;
}