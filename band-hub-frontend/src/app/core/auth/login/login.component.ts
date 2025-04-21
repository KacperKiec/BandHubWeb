import { Component } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private router: Router) {
  }

  confirmLogin() {
    console.log('Loging In')
  }

  remindPassword() {
    console.log('Remind Password')
    this.router.navigate(['/auth/remind'])
  }

  register() {
    console.log('Registering')
    this.router.navigate(['/auth/register'])
  }
}
