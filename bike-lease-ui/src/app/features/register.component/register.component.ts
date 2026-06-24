import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { AuthService } from '../../shared/services/auth.service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-register.component',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {

  registerForm!: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {

    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      role: ['USER', Validators.required]
    });
  }

  register() {
    if (this.registerForm.invalid) {
      return;
    }

    const password =
      this.registerForm.value.password;

    const confirmPassword =
      this.registerForm.value.confirmPassword;

    if (password !== confirmPassword) {

      alert('Passwords do not match');

      return;
    }

    this.authService.register({
      email: this.registerForm.value.email!,
      password: password!,
      role: this.registerForm.value.role!
    }).subscribe({

      next: () => {
        this.router.navigate(['/']);
      },

      error: err => {
        console.error(err);
      }
    });
  }
}
