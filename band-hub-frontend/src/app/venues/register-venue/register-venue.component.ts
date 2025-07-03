import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BandInputComponent} from '../../shared/band-input/band-input.component';
import {AddressSearchComponent} from '../../shared/address-search/address-search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {Address} from '../../core/models/location.model';
import {Venue, VenueRequest} from '../../core/models/venue.model';
import {VenueInputComponent} from '../../shared/venue-input/venue-input.component';

@Component({
  selector: 'app-register-venue',
  standalone: true,
  imports: [
    BandInputComponent,
    AddressSearchComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    VenueInputComponent
  ],
  templateUrl: './register-venue.component.html',
  styleUrl: './register-venue.component.css'
})
export class RegisterVenueComponent implements OnInit {
  @Input() userId!: number;
  @Output() registered: EventEmitter<VenueRequest> = new EventEmitter();

  venue!: VenueRequest;

  constructor() {
  }

  ngOnInit() {
    this.venue = {
      ownerId: this.userId,
      name: '',
      capacity: 0,
      description: '',
      street: '',
      number: '',
      city: '',
      postalCode: '',
      country: ''
    };
  }

  getVenue(value: VenueRequest) {
    this.venue = {
      ownerId: this.userId,
      name: value.name,
      capacity: value.capacity,
      description: value.description,
      street: value.street,
      number: value.number,
      city: value.city,
      postalCode: value.postalCode,
      country: value.country
    }
  }

  submit() {
    console.log('Lokal zarejestrowany' + this.venue);
    this.registered.emit(this.venue);
  }
}
