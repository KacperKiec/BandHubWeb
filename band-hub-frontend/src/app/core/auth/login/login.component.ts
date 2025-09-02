import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {AuthApiService} from '../auth-service/auth-api.service';
import {AuthService} from '../service/auth.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';

  constructor(private router: Router, private authApiService: AuthApiService, private authService: AuthService) {
  }

  confirmLogin() {
    this.authApiService.login(this.email, this.password).subscribe({
      next: () => {
        this.authService.setLoginStatus(true);
        this.router.navigate(['/']);
      }
    });
  }

  remindPassword() {
    this.router.navigate(['/auth/remind'])
  }

  register() {
    this.router.navigate(['/auth/register'])
  }
}
