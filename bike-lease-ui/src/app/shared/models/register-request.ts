export type UserRole = 'USER' | 'ADMIN';

export interface RegisterRequest {
  email: string;
  password: string;
  role: UserRole;
}
