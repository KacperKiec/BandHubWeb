import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Address} from '../../core/models/location.model';
import {AccountPersonalData} from '../../core/models/account.model';
import {FormsModule} from '@angular/forms';
import {AddressSearchComponent} from '../address-search/address-search.component';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-personal-info-input',
  standalone: true,
  imports: [
    FormsModule,
    AddressSearchComponent,
    NgForOf
  ],
  templateUrl: './personal-info-input.component.html',
  styleUrl: './personal-info-input.component.css'
})
export class PersonalInfoInputComponent implements OnInit {

  @Input() existingAccount!: AccountPersonalData;
  @Output() accountChange: EventEmitter<AccountPersonalData> = new EventEmitter();

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
    day: null as number | null,
    month: null as number | null,
    year: null as number | null,
    gender: null as boolean | null,
    city: '',
    country: ''
  };

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
    if(this.existingAccount) {
      this.formData.firstName = this.existingAccount.firstName;
      this.formData.lastName = this.existingAccount.lastName;
      this.formData.city = this.existingAccount.city;
      this.formData.country = this.existingAccount.country;
      this.formData.gender = this.existingAccount.gender;
      this.formData.day = this.existingAccount.day;
      this.formData.month = this.existingAccount.month;
      this.formData.year = this.existingAccount.year;
    }
  }

  getAddress(address: Address) {
    this.formData.city = address.city;
    this.formData.country = address.country;
    this.onInputChange();
  }

  sendAddress(): Address {
    return {
      street: '',
      number: '',
      city: this.formData.city,
      postalCode: '',
      country: this.formData.country,
      latitude: 0,
      longitude: 0
    };
  }


  onInputChange() {
    if (this.formData.month && this.formData.year && this.formData.day) {
      if (!this.isValidDay(this.formData.month, this.formData.year, this.formData.day)) {
        alert('Błędna data urodzenia!');
        this.formData.day = null;
        this.formData.month = null;
        this.formData.year = null;
        return;
      }
    }

    let account: AccountPersonalData = {
      firstName: this.formData.firstName,
      lastName: this.formData.lastName,
      gender: this.formData.gender,
      day: this.formData.day,
      month: this.formData.month,
      year: this.formData.year,
      city: this.formData.city,
      country: this.formData.country
    };

    this.accountChange.emit(account);

  }

  isValidDay(month: number, year: number, day: number): boolean {
    const daysInMonth = new Date(year, month, 0).getDate();
    return day <= daysInMonth;
  }
}
