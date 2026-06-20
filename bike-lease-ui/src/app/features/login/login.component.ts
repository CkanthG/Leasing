import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../shared/services/auth.service';
import {Router} from '@angular/router';
import { UserService } from '../../shared/services/user.service';

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
    private userService: UserService,
    private router: Router
  ) {}

  login() {

    this.authService.login({
        email: this.email,
        password: this.password
      }).subscribe(
      {
        next: (response: any) => {
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);

          this.userService.getUserRoleByToken().subscribe(
            {
              next: response => {
                localStorage.setItem("role", response.role);
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
