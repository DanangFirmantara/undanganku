import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { LoginComponent } from './features/auth/login.component';
import { AppLayoutComponent } from './layout/app-layout.component';
import { AdminDashboardComponent } from './features/dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './features/dashboard/user-dashboard.component';
import { UserListComponent } from './features/users/user-list.component';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () =>
      import('./landing/landing.component').then(m => m.LandingComponent),
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'app',
    component: AppLayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard',
      },
      {
        path: 'dashboard',
        children: [
          {
            path: 'admin',
            component: AdminDashboardComponent,
          },
          {
            path: 'user',
            component: UserDashboardComponent,
          },
          {
            path: '',
            redirectTo: 'user',
            pathMatch: 'full',
          },
        ],
      },
      {
        path: 'users',
        component: UserListComponent,
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];

