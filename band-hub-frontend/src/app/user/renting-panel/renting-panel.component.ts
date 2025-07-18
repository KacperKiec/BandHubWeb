import {Component, OnInit} from '@angular/core';
import {EditAccountFormComponent} from "../edit/edit-account-form/edit-account-form.component";
import {EditMusicianFormComponent} from "../edit/edit-musician-form/edit-musician-form.component";
import {EditPasswordFormComponent} from "../edit/edit-password-form/edit-password-form.component";
import {NgIf, NgSwitch, NgSwitchCase} from "@angular/common";
import {UserVenuesComponent} from '../user-venues/user-venues.component';
import {UserOffersComponent} from '../user-offers/user-offers.component';
import {VenueApiService} from '../../services/venue-service/venue-api.service';
import {Venue} from '../../core/models/venue.model';
import {Offer} from '../../core/models/offer.model';
import {OfferApiService} from '../../services/offer-service/offer-api.service';

@Component({
  selector: 'app-renting-panel',
  standalone: true,
  imports: [
    EditAccountFormComponent,
    EditMusicianFormComponent,
    EditPasswordFormComponent,
    NgIf,
    UserVenuesComponent,
    UserOffersComponent,
    NgSwitch,
    NgSwitchCase
  ],
  templateUrl: './renting-panel.component.html',
  styleUrl: './renting-panel.component.css'
})
export class RentingPanelComponent implements OnInit {

  activeTab: string = 'venues';
  userId!: number;
  venues: Venue[] = [];
  offers: Offer[] = [];

  constructor(private venueApiService: VenueApiService, private offerApiService: OfferApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.initData()
  }

  initData() {
    this.venueApiService.getUserVenues(this.userId).subscribe(venues => {
      if (venues) {
        this.venues = [...venues];
      }
    });

    this.offerApiService.getUserOffers(this.userId).subscribe(offers => {
      if (offers) {
        this.offers = [...offers];
        console.log(this.offers);
      }
    });
  }

  updateData($event: boolean) {
    if ($event) this.initData();
  }
}
