import { Routes } from '@angular/router';
import {LoginComponent} from './features/login/login.component';
import {DashboardComponent} from './features/dashboard/dashboard.component';
import {authGuard} from './guards/auth-guard';
import { UserListComponent } from './features/users/user-list/user-list.component';
import { BikeListComponent } from './features/bike-inventory/bike-list/bike-list.component';
import { RegisterComponent } from './features/register.component/register.component';
import { LeaseComponent } from './features/lease.component/lease.component';

export const routes: Routes = [
  {
    path: '',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  {
    path: 'users',
    component: UserListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'inventory',
    component: BikeListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'leases',
    component: LeaseComponent,
    canActivate: [authGuard]
  }
];
