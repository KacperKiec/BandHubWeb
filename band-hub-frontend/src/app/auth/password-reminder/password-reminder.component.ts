import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthApiService} from '../auth-service/auth-api.service';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-password-reminder',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './password-reminder.component.html',
  styleUrl: './password-reminder.component.css'
})
export class PasswordReminderComponent {
  email: string = '';
  isConfirmed: boolean = false;

  constructor(private authApiService: AuthApiService) {}

  confirm() {
    if (this.email) {
      this.authApiService.remindPassword(this.email).subscribe({
        next: () => {
          this.isConfirmed = true;
        }
      });
    }
  }
}
