import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {

  email = ''
  password = ''

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login() {

    this.authService.login({
        email: this.email,
        password: this.password
      }).subscribe(
      {
        next: response => {
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);

          this.router.navigate([
            '/dashboard'
          ])
        },
        error: error => {
          alert('Invalid Credentials');
        }
      }
    )
  }

}
