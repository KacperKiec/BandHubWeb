import {Component, OnInit} from '@angular/core';
import {AddressSearchComponent} from '../../../shared/address-search/address-search.component';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {PersonalInfoInputComponent} from '../../../shared/personal-info-input/personal-info-input.component';
import {AccountPersonalData, AccountResponse, AccountUpdateRequest} from '../../../core/models/account.model';
import {UserApiService} from '../../../services/user-service/user-api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-edit-account-form',
  standalone: true,
  imports: [
    AddressSearchComponent,
    FormsModule,
    NgForOf,
    PersonalInfoInputComponent,
    NgIf
  ],
  templateUrl: './edit-account-form.component.html',
  styleUrl: './edit-account-form.component.css'
})
export class EditAccountFormComponent implements OnInit{

  userId!: number;
  account!: AccountResponse;

  constructor(private userApiService: UserApiService, private router: Router) {
  }

  formData = {
    firstName: '',
    lastName: '',
    birthDate: '',
    gender: null as boolean | null,
    city: '',
    country: ''
  };

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }


    this.userApiService.getCurrentUser().subscribe(acc => {
      if(acc) {
        acc.birthDate = new Date(acc.birthDate);
        this.account = acc;
      }
    })
  }

  getData(value: AccountPersonalData) {
    this.formData.firstName = value.firstName;
    this.formData.lastName = value.lastName;
    this.formData.birthDate = `${value.year}-${String(value.month).padStart(2, '0')}-${String(value.day).padStart(2, '0')}`;
    this.formData.city = value.city;
    this.formData.country = value.country;
    this.formData.gender = value.gender;
  }

  sendPersonalData(): AccountPersonalData {
    return {
      firstName: this.account.firstName,
      lastName: this.account.lastName,
      day: this.account.birthDate.getDate(),
      month: this.account.birthDate.getMonth() + 1,
      year: this.account.birthDate.getFullYear(),
      gender: this.account.gender,
      city: this.account.city,
      country: this.account.country
    };
  }

  submitForm() {
    for (let value of Object.values(this.formData)) {
      if (value === null || value === '') {
        alert('Uzupełnij wszystkie wymagane pola!');
        return;
      }
    }

    let account: AccountUpdateRequest = {
      id: this.userId,
      firstName: this.formData.firstName,
      lastName: this.formData.lastName,
      gender: this.formData.gender === true,
      birthDate: this.formData.birthDate,
      city: this.formData.city,
      country: this.formData.country
    };

    console.log(account)
    this.userApiService.updateUserData(account).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      }
    });
  }
}
