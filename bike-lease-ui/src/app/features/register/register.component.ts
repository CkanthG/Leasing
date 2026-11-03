import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { UserRole } from '../../shared/models/register-request';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  email = '';
  password = '';
  confirmPassword = '';
  role: UserRole = 'USER';
  errorMessage = '';
  registered = false;
  submitting = false;

  constructor(private authService: AuthService) {}

  register(): void {
    this.errorMessage = '';

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.submitting = true;
    this.authService.register({
      email: this.email.trim(),
      password: this.password,
      role: this.role,
    }).subscribe({
      next: () => {
        this.registered = true;
        this.submitting = false;
      },
      error: (error: { error?: { message?: string } }) => {
        this.errorMessage = error.error?.message ?? 'Registration failed. Please try again.';
        this.submitting = false;
      },
    });
  }
}
