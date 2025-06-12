import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BandInputComponent} from '../../shared/band-input/band-input.component';
import {AddressSearchComponent} from '../../shared/address-search/address-search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {Address} from '../../core/models/location.model';
import {VenueRequest} from '../../core/models/venue.model';

@Component({
  selector: 'app-register-venue',
  standalone: true,
  imports: [
    BandInputComponent,
    AddressSearchComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './register-venue.component.html',
  styleUrl: './register-venue.component.css'
})
export class RegisterVenueComponent {
  @Input() userId!: number;
  @Output() registered: EventEmitter<VenueRequest> = new EventEmitter();

  constructor() {
  }

  sendAddress(): Address {
    return {
      street: '',
      number: '',
      city: '',
      postalCode: '',
      country: '',
      latitude: 0,
      longitude: 0
    };
  }

  getAddress(address: Address) {

  }

  submit() {

  }
}
