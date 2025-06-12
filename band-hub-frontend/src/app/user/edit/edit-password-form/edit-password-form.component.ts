import { Component } from '@angular/core';
import {CredentialsInputComponent} from '../../../shared/credentials-input/credentials-input.component';
import {FormsModule} from '@angular/forms';
import {UserApiService} from '../../../services/user-service/user-api.service';

@Component({
  selector: 'app-edit-password-form',
  standalone: true,
  imports: [
    CredentialsInputComponent,
    FormsModule
  ],
  templateUrl: './edit-password-form.component.html',
  styleUrl: './edit-password-form.component.css'
})
export class EditPasswordFormComponent {

  constructor(private userApiService: UserApiService) {
  }

  formData = {
    oldPassword: '',
    newPassword: '',
    repeatPassword: ''
  };

  changePassword() {
    const oldPassword = this.formData.oldPassword ?? '';
    const password: string = this.formData.newPassword ?? '';
    const repeatPassword: string = this.formData.repeatPassword ?? '';

    if (!password || !repeatPassword) {
      alert('Nowe hasło nie może być puste!');
      return;
    }

    if (password.length < 8) {
      alert('Hasło powinno zawierać co najmniej 8 znaków!');
      return;
    }

    if (password !== repeatPassword) {
      alert('Hasła się nie zgadzają!');
      return;
    }

    this.userApiService.checkPassword(oldPassword).subscribe({
      next: (isValid: boolean) => {
        if (isValid) {
          this.userApiService.changePassword(password).subscribe({
            next: () => {
              alert('Hasło zostało zmienione pomyślnie!');
              this.formData = { oldPassword: '', newPassword: '', repeatPassword: '' };
            },
              error: (err) => {
              alert('Nie udało się zmienić hasła.');
            }
          });
        } else {
          alert('Stare hasło jest nieprawidłowe.');
        }
      },
      error: (err) => {
        console.error('Błąd podczas sprawdzania hasła:', err);
        alert('Wystąpił błąd podczas sprawdzania hasła.');
      }
    });
  }

}
