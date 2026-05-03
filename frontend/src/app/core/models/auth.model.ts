import { User } from './user.model';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  expiresIn: number;
  user: User;
}

export interface AuthState {
  isLoggedIn: boolean;
  user: User | null;
  token: string | null;
  expiresAt: number | null;
}

