import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../shared/services/auth.service';
import {Router, RouterLink} from '@angular/router';
import { UserService } from '../../shared/services/user.service';
import { MatCard, MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule, 
    MatCard, 
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCardModule,
    ReactiveFormsModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {

  loginForm!: FormGroup

  email = ''
  password = ''

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  login() {

    if (this.loginForm.invalid) {
      return;
    }

    this.authService.login({
        email: this.loginForm.value.email!,
        password: this.loginForm.value.password!
      }).subscribe(
      {
        next: (response: any) => {
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);

          this.userService.getUserRoleByToken().subscribe(
            {
              next: response => {
                localStorage.setItem("role", response.role);
                localStorage.setItem("email", response.email);
              },
              error: err => {
                alert('Invalid Access Token');
              }
            }
          )

          this.router.navigate([
            '/dashboard'
          ])
        },
        error: (error: any) => {
          alert('Invalid Credentials');
        }
      }
    )
  }

}
