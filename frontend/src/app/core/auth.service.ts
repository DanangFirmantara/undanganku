import { Injectable, signal, computed, effect } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { LoginRequest, LoginResponse, AuthState } from './models/auth.model';
import { User } from './models/user.model';

const TOKEN_KEY = 'auth_token';
const USER_KEY = 'auth_user';
const EXPIRES_KEY = 'auth_expires';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = '/api/auth';

  private authStateSignal = signal<AuthState>({
    isLoggedIn: this.hasValidToken(),
    user: this.loadStoredUser(),
    token: this.loadStoredToken(),
    expiresAt: this.loadStoredExpiresAt(),
  });

  public authState = this.authStateSignal.asReadonly();
  public isLoggedIn = computed(() => this.authState().isLoggedIn);
  public currentUser = computed(() => this.authState().user);

  private autoLogoutTimer: any;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    effect(() => {
      if (this.authState().expiresAt) {
        this.startAutoLogout();
      }
    });
  }

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, request)
      .subscribe({
        next: (response) => {
          this.setAuthState(response);
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          console.error('Login failed', error);
        },
      });
  }

  logout() {
    this.http.post(`${this.apiUrl}/logout`, {})
      .subscribe({
        complete: () => {
          this.clearAuthState();
          this.router.navigate(['/login']);
        },
      });
  }

  private setAuthState(response: LoginResponse) {
    const expiresAt = Date.now() + response.expiresIn;

    this.authStateSignal.set({
      isLoggedIn: true,
      user: response.user,
      token: response.token,
      expiresAt,
    });

    localStorage.setItem(TOKEN_KEY, response.token);
    localStorage.setItem(USER_KEY, JSON.stringify(response.user));
    localStorage.setItem(EXPIRES_KEY, expiresAt.toString());
  }

  private clearAuthState() {
    this.authStateSignal.set({
      isLoggedIn: false,
      user: null,
      token: null,
      expiresAt: null,
    });

    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    localStorage.removeItem(EXPIRES_KEY);

    if (this.autoLogoutTimer) {
      clearTimeout(this.autoLogoutTimer);
    }
  }

  private loadStoredToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private loadStoredUser(): User | null {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  private loadStoredExpiresAt(): number | null {
    const expiresStr = localStorage.getItem(EXPIRES_KEY);
    return expiresStr ? parseInt(expiresStr, 10) : null;
  }

  private hasValidToken(): boolean {
    const token = this.loadStoredToken();
    const expiresAt = this.loadStoredExpiresAt();

    if (!token || !expiresAt) return false;

    return Date.now() < expiresAt;
  }

  private startAutoLogout() {
    const expiresAt = this.authState().expiresAt;
    if (!expiresAt) return;

    const timeUntilExpiry = expiresAt - Date.now();

    if (this.autoLogoutTimer) {
      clearTimeout(this.autoLogoutTimer);
    }

    this.autoLogoutTimer = setTimeout(() => {
      this.logout();
    }, timeUntilExpiry);
  }
}

