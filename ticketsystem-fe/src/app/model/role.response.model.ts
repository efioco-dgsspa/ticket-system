import { Role } from "./role.model";

export interface RoleResponse {
  role?: Role;
  roles?: Role[];
  message?: string;
}