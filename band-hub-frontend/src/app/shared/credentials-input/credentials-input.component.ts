import {Component, EventEmitter, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AccountCredentials} from '../../core/models/account.model';

@Component({
  selector: 'app-credentials-input',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './credentials-input.component.html',
  styleUrl: './credentials-input.component.css'
})
export class CredentialsInputComponent {

  @Output() credentials: EventEmitter<AccountCredentials> = new EventEmitter();

  formData = {
    email: '',
    password: '',
    repeatPassword: ''
  };

  validatePasswords() {
    const password: string = this.formData.password ?? '';
    const repeatPassword: string = this.formData.repeatPassword ?? '';

    if (!password || !repeatPassword) {
      return;
    }

    if (password.length < 8) {
      alert('Hasło powinno zawierać co najmniej 8 znaków!');
      return;
    }

    if (password !== repeatPassword) {
      alert('Niepoprawne hasło!');
      return;
    }

    this.onInputChange();
  }

  onInputChange() {

    let cred: AccountCredentials = {
      email: this.formData.email,
      password: this.formData.password
    }

    this.credentials.emit(cred);
  }
}
