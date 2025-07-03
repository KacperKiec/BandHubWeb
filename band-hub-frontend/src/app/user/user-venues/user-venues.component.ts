import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {Venue, VenueRequest} from '../../core/models/venue.model';
import {VenueApiService} from '../../services/venue-service/venue-api.service';

@Component({
  selector: 'app-user-venues',
  standalone: true,
  imports: [
    NgIf,
    RegisterBandComponent,
    RegisterVenueComponent,
    NgForOf
  ],
  templateUrl: './user-venues.component.html',
  styleUrl: './user-venues.component.css'
})
export class UserVenuesComponent implements OnInit {

  userId!: number;
  hasVenues: boolean = false;
  showForm: boolean = false;

  venues!:Venue[];

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
        this.venues = venues;
        this.hasVenues = this.venues.length > 0;
      }
    });
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  viewVenueDetails(venueId: number) {}

  goToVenues() {

  }

  handleVenueRegistration(venue: VenueRequest) {
    console.log(venue);
    this.venueApiService.addVenue(venue).subscribe(venue => {
      if (venue) {
        this.showForm = false;
        this.venues.push(venue);
        this.hasVenues = true;
      }
    })
  }
}
