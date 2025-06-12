import { Component } from '@angular/core';
import {NgIf} from '@angular/common';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {VenueRequest} from '../../core/models/venue.model';

@Component({
  selector: 'app-user-venues',
  standalone: true,
  imports: [
    NgIf,
    RegisterBandComponent,
    RegisterVenueComponent
  ],
  templateUrl: './user-venues.component.html',
  styleUrl: './user-venues.component.css'
})
export class UserVenuesComponent {

  userId!: number;
  hasVenues: boolean = false;
  showForm: boolean = false;

  constructor() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.initData()
  }

  initData() {

  }

  showRegistrationForm() {
    this.showForm = true;
  }

  goToVenues() {

  }

  handleVenueRegistration(venue: VenueRequest) {

  }
}
