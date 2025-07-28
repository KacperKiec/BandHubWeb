import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {VenueRequest} from '../../core/models/venue.model';
import {VenueInputComponent} from '../venue-input/venue-input.component';

@Component({
  selector: 'app-register-venue',
  standalone: true,
  imports: [
    FormsModule,
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
      country: '',
      latitude: 0,
      longitude: 0
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
      country: value.country,
      latitude: value.latitude,
      longitude: value.longitude,
    }
  }

  submit() {
    console.log('Lokal zarejestrowany' + this.venue);
    this.registered.emit(this.venue);
  }
}
