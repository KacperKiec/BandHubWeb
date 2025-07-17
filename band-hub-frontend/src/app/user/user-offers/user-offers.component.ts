import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {NgIf} from '@angular/common';
import {Venue} from '../../core/models/venue.model';
import {RegisterOfferComponent} from '../../offers/register-offer/register-offer.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {OfferRequest} from '../../core/models/offer.model';

@Component({
  selector: 'app-user-offers',
  standalone: true,
  imports: [
    NgIf,
    RegisterOfferComponent,
    RegisterVenueComponent
  ],
  templateUrl: './user-offers.component.html',
  styleUrl: './user-offers.component.css'
})
export class UserOffersComponent implements OnChanges {
  @Input() venues!: Venue[];
  @Input() userId!: number;
  @Output() updateData = new EventEmitter<boolean>();

  hasVenues: boolean = false;
  hasOffers: boolean = false;
  showForm: boolean = false;

  constructor(private cdr: ChangeDetectorRef) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['venues']) {
      this.hasVenues = this.venues && this.venues.length > 0;
    }

    this.cdr.markForCheck();
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  handleOfferRegistration(offer: OfferRequest) {

  }
}
