import {Component, input, OnInit} from '@angular/core';
import {NgForOf} from '@angular/common';
import { FormsModule} from '@angular/forms';
import {AddressSearchComponent} from '../../../shared/address-search/address-search.component';
import {Account, Address} from '../../models/types';
import {AuthApiService} from '../../../services/auth-service/auth-api.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    AddressSearchComponent,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit{
  days: number[] = Array.from({ length: 31 }, (_, i) => i + 1);
  months = [
    { value: 1, name: 'Styczeń' },
    { value: 2, name: 'Luty' },
    { value: 3, name: 'Marzec' },
    { value: 4, name: 'Kwiecień' },
    { value: 5, name: 'Maj' },
    { value: 6, name: 'Czerwiec' },
    { value: 7, name: 'Lipiec' },
    { value: 8, name: 'Sierpień' },
    { value: 9, name: 'Wrzesień' },
    { value: 10, name: 'Październik' },
    { value: 11, name: 'Listopad' },
    { value: 12, name: 'Grudzień' }
  ];
  years: number[] = [];

  formData = {
    firstName: '',
    lastName: '',
    day: null,
    month: null,
    year: null,
    gender: null as boolean | null,
    city: '',
    country: '',
    email: '',
    password: '',
    repeatPassword: ''
  };

  constructor(private authApiService: AuthApiService) {
  }

  ngOnInit() {
    const currentYear = new Date().getFullYear();
    const startYear = 1920;
    this.years = Array.from({ length: currentYear - startYear + 1 }, (_, i) => currentYear - i);

    setTimeout(() => {
      this.initializeDefaultValues();
    }, 0);

    setTimeout(() => {
      document.body.click();
    });
  }

  initializeDefaultValues() {
  }

  getAddress(address: Address) {
    this.formData.city = address.city;
    this.formData.country = address.country;
  }


  submitForm() {
    console.log('Dane formularza:', this.formData);

    for (let value of Object.values(this.formData)) {
      if (!value) {
        alert('Uzupełnij wszystkie wymagane pola!');
        return;
      }
    }

    if (this.formData.month && this.formData.year && this.formData.day) {
      if (!this.isValidDay(this.formData.month, this.formData.year, this.formData.day)) {
        alert('Błędna data urodzenia!');
        return;
      }
    }

    if(this.formData.password.length < 8) {
      alert('Hasło powinno zawierać co najmniej 8 znaków!')
      return;
    }

    if (this.formData.password !== this.formData.repeatPassword) {
      alert('Niepoprawne hasło!');
      return;
    }

    let account: Account = {
      firstName: this.formData.firstName,
      lastName: this.formData.lastName,
      email: this.formData.email,
      password: this.formData.password,
      gender: this.formData.gender === true,
      birthday: new Date(`${this.formData.year}-${this.formData.month}-${this.formData.day}`),
      city: this.formData.city,
      country: this.formData.country
    };

    console.log(account)
    //this.authApiService.registerAccount(account);
  }

  isValidDay(month: number, year: number, day: number): boolean {
    const daysInMonth = new Date(year, month, 0).getDate();
    return day <= daysInMonth;
  }

  protected readonly input = input;
}
