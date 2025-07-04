import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {Venue, VenueRequest, VenueUpdateRequest} from '../../core/models/venue.model';
import {VenueApiService} from '../../services/venue-service/venue-api.service';
import {EditBandComponent} from '../edit/edit-band/edit-band.component';
import {EditVenueFormComponent} from '../edit/edit-venue-form/edit-venue-form.component';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-user-venues',
  standalone: true,
  imports: [
    NgIf,
    RegisterBandComponent,
    RegisterVenueComponent,
    NgForOf,
    EditBandComponent,
    EditVenueFormComponent
  ],
  templateUrl: './user-venues.component.html',
  styleUrl: './user-venues.component.css',
  animations: [
    trigger('expandCollapse', [
      state('collapsed', style({
        height: '0px',
        overflow: 'hidden',
        opacity: 0
      })),
      state('expanded', style({
        height: '*',
        overflow: 'hidden',
        opacity: 1
      })),
      transition('collapsed <=> expanded', [
        animate('300ms ease-in-out')
      ]),
    ])
  ]
})
export class UserVenuesComponent implements OnInit {

  userId!: number;
  hasVenues: boolean = false;
  showForm: boolean = false;
  editingMode: boolean = false;

  venues!:Venue[];
  venueToEdit!: Venue;
  expandedVenueId: number | null = null;

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
        console.log(this.venues);
      }
    });
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  addAnotherVenue() {
    this.showForm = true;
    this.hasVenues = false;
  }

  viewVenueDescription(venueId: number) {
    this.expandedVenueId = this.expandedVenueId === venueId ? null : venueId;
  }

  goToVenues() {

  }

  deleteVenue(id: number) {
    this.venueApiService.deleteVenue(id).subscribe({
      next: () => {
        this.initData();
      },
      error: (err) => {
        console.error('Błąd podczas usuwania lokalu:', err);
      }
    });
  }

  turnEditingMode(venue: Venue) {
    this.editingMode = true;
    this.venueToEdit = venue;
    console.log(this.venueToEdit);
  }

  handleVenueRegistration(venue: VenueRequest) {
    this.venueApiService.addVenue(venue).subscribe(venue => {
      if (venue) {
        this.showForm = false;
        this.venues.push(venue);
        this.hasVenues = true;
      }
    });
  }

  handleVenueUpdating(venue: VenueUpdateRequest) {
    this.venueApiService.updateVenue(venue).subscribe(venue => {
      if (venue) {
        this.editingMode = false;
        this.initData();
      }
    });
  }
}
