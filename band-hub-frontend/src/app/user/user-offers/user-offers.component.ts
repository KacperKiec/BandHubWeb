import {Component, Input, OnInit} from '@angular/core';
import {NgIf} from '@angular/common';
import {VenueApiService} from '../../services/venue-service/venue-api.service';
import {Venue} from '../../core/models/venue.model';

@Component({
  selector: 'app-user-offers',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './user-offers.component.html',
  styleUrl: './user-offers.component.css'
})
export class UserOffersComponent implements OnInit {

  hasVenues: boolean = false;
  hasOffers: boolean = false;
  showForm: boolean = false;

  @Input() venues!: Venue[];

  ngOnInit() {

  }

  showRegistrationForm() {
    this.showForm = true;
  }

  handleOfferRegistration() {

  }
}
