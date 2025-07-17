import {Component, OnInit} from '@angular/core';
import {EditAccountFormComponent} from "../edit/edit-account-form/edit-account-form.component";
import {EditMusicianFormComponent} from "../edit/edit-musician-form/edit-musician-form.component";
import {EditPasswordFormComponent} from "../edit/edit-password-form/edit-password-form.component";
import {NgIf} from "@angular/common";
import {UserVenuesComponent} from '../user-venues/user-venues.component';
import {UserOffersComponent} from '../user-offers/user-offers.component';
import {VenueApiService} from '../../services/venue-service/venue-api.service';
import {Venue} from '../../core/models/venue.model';

@Component({
  selector: 'app-renting-panel',
  standalone: true,
  imports: [
    EditAccountFormComponent,
    EditMusicianFormComponent,
    EditPasswordFormComponent,
    NgIf,
    UserVenuesComponent,
    UserOffersComponent
  ],
  templateUrl: './renting-panel.component.html',
  styleUrl: './renting-panel.component.css'
})
export class RentingPanelComponent implements OnInit {

  activeTab: string = 'venues';
  userId!: number;
  venues!: Venue[];

  constructor(private venueApiService: VenueApiService) {
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
  }

  updateData($event: boolean) {
    if ($event) this.initData();
  }
}
