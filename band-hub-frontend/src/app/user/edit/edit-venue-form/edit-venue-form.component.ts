import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Venue, VenueRequest, VenueUpdateRequest} from '../../../core/models/venue.model';
import {BandInputComponent} from '../../../shared/band-input/band-input.component';
import {FormsModule} from '@angular/forms';
import {VenueInputComponent} from '../../../shared/venue-input/venue-input.component';

@Component({
  selector: 'app-edit-venue-form',
  standalone: true,
  imports: [
    BandInputComponent,
    FormsModule,
    VenueInputComponent
  ],
  templateUrl: './edit-venue-form.component.html',
  styleUrl: './edit-venue-form.component.css'
})
export class EditVenueFormComponent implements OnInit {
  @Input() userId!: number;
  @Input() venueTemplate!:Venue;
  @Output() updated: EventEmitter<VenueUpdateRequest> = new EventEmitter();

  venue!: VenueUpdateRequest;

  ngOnInit() {
    if(this.venueTemplate) {
      this.venue = {
        id: this.venueTemplate.id,
        ownerId: this.userId,
        name: this.venueTemplate.name,
        capacity: this.venueTemplate.capacity,
        description: this.venueTemplate.description,
        street: this.venueTemplate.street,
        number: this.venueTemplate.number,
        city: this.venueTemplate.city,
        postalCode: this.venueTemplate.postalCode,
        country: this.venueTemplate.country,
      };
    }
  }

  getVenue(value: VenueRequest) {
    this.venue = {
      id: this.venueTemplate.id,
      ownerId: this.userId,
      name: value.name,
      capacity: value.capacity,
      description: value.description,
      street: value.street,
      number: value.number,
      city: value.city,
      postalCode: value.postalCode,
      country: value.country,
    };
  }

  submitForm() {
    this.updated.emit(this.venue);
  }
}
