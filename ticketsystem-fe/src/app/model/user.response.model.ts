import { User } from "./user.model";

export interface UserResponse {
  user?: User;
  users?: User[];
  message?: string;
}