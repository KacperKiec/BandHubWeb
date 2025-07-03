import {Component, EventEmitter, Input, OnInit, Output, SimpleChanges} from '@angular/core';
import {AddressSearchComponent} from "../address-search/address-search.component";
import {FormsModule} from "@angular/forms";
import {Address} from '../../core/models/location.model';
import {VenueRequest} from '../../core/models/venue.model';

@Component({
  selector: 'app-venue-input',
  standalone: true,
    imports: [
        AddressSearchComponent,
        FormsModule
    ],
  templateUrl: './venue-input.component.html',
  styleUrl: './venue-input.component.css'
})
export class VenueInputComponent implements OnInit {

  @Input() userId!: number;
  @Input() inputVenue!: VenueRequest;
  @Output() outputVenue: EventEmitter<VenueRequest> = new EventEmitter();

  venue!: VenueRequest;

  ngOnInit() {
    this.initVenue()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputVenue'] && changes['inputVenue'].currentValue) {
      this.initVenue();
    }
  }

  private initVenue() {
    this.venue = this.inputVenue ?? {
      ownerId: this.userId,
      name: '',
      capacity: 0,
      description: '',
      street: '',
      number: '',
      city: '',
      postalCode: '',
      country: ''
    }
  }

  sendAddress(): Address {
    return {
      street: this.venue.street,
      number: this.venue.number,
      city: this.venue.city,
      postalCode: this.venue.postalCode,
      country: this.venue.country,
      latitude: 0,
      longitude: 0
    };
  }

  getAddress(address: Address) {
    this.venue.street = address.street;
    this.venue.number = address.number;
    this.venue.city = address.city;
    this.venue.country = address.country;
    this.venue.postalCode = address.postalCode;
    this.emit();
  }

  emit() {
    this.outputVenue.emit(this.venue);
  }
}
