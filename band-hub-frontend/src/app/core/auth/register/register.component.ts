import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import { FormsModule} from '@angular/forms';
import {AddressSearchComponent} from '../../../shared/address-search/address-search.component';
import {AuthApiService} from '../../../services/auth-service/auth-api.service';
import {Router} from '@angular/router';
import {AuthService} from '../service/auth.service';
import {AccountCredentials, AccountPersonalData, AccountRegisterRequest} from '../../models/account.model';
import {PersonalInfoInputComponent} from '../../../shared/personal-info-input/personal-info-input.component';
import {CredentialsInputComponent} from '../../../shared/credentials-input/credentials-input.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    AddressSearchComponent,
    NgIf,
    PersonalInfoInputComponent,
    CredentialsInputComponent,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{

  formData = {
    firstName: '',
    lastName: '',
    birthDate: '',
    gender: null as boolean | null,
    city: '',
    country: '',
    email: '',
    password: ''
  };

  constructor(private authApiService: AuthApiService, private router: Router, private authService: AuthService) {
  }

  ngOnInit() {
    setTimeout(() => {
      this.initializeDefaultValues();
    }, 0);

    setTimeout(() => {
      document.body.click();
    });
  }

  initializeDefaultValues() {
  }

  getPersonalData(value: AccountPersonalData) {
    this.formData.firstName = value.firstName;
    this.formData.lastName = value.lastName;
    this.formData.birthDate = `${value.year}-${String(value.month).padStart(2, '0')}-${String(value.day).padStart(2, '0')}`;
    this.formData.city = value.city;
    this.formData.country = value.country;
    this.formData.gender = value.gender;
  }

  getCredentials(value: AccountCredentials) {
    this.formData.email = value.email;
    this.formData.password = value.password;
  }

  submitForm() {
    console.log('Dane formularza:', this.formData);

    for (let value of Object.values(this.formData)) {
      if (value === null || value === '') {
        alert('Uzupełnij wszystkie wymagane pola!');
        return;
      }
    }

    let account: AccountRegisterRequest = {
      firstName: this.formData.firstName,
      lastName: this.formData.lastName,
      email: this.formData.email,
      password: this.formData.password,
      gender: this.formData.gender === true,
      birthDate: this.formData.birthDate,
      city: this.formData.city,
      country: this.formData.country,
      roles: [{name: 'USER'}]
    };

    console.log(account)
    this.authApiService.registerAccount(account).subscribe({
      next: () => {
        this.authApiService.login(account.email, account.password).subscribe({
          next: () => {
            this.authService.setLoginStatus(true);
            this.router.navigate(['/']);
          },
          error: (loginErr) => {
            console.error('Logowanie nie powiodło się:', loginErr);
            alert(loginErr);
          }
        });
      },
      error: (err) => {
        console.error('Błąd rejestracji:', err);
        alert(err);
      }
    });
  }
}
