import {Component, OnInit} from '@angular/core';
import {CalendarComponent} from '../../shared/calendar/calendar.component';
import {EventDTO, EventRequestDTO, EventUpdateRequestDTO, UserEventDTO} from '../../core/models/event.model';
import {EventApiService} from '../event-service/event-api.service';
import {NgIf} from '@angular/common';
import {RegisterEventComponent} from '../register-event/register-event.component';
import {EditEventFormComponent} from '../edit-event-form/edit-event-form.component';
import {BookingApiService} from '../../bookings/booking-service/booking-api.service';
import {Band} from '../../core/models/band.model';
import {Booking} from '../../core/models/booking.model';
import {MusicianApiService} from '../../musicians/musician-service/musician-api.service';
import {VenueApiService} from '../../venues/venue-service/venue-api.service';
import {Venue} from '../../core/models/venue.model';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-user-events',
  standalone: true,
  imports: [
    CalendarComponent,
    NgIf,
    RegisterEventComponent,
    EditEventFormComponent
  ],
  templateUrl: './user-events.component.html',
  styleUrl: './user-events.component.css',
  animations: [
    trigger('backdropFadeInOut', [
      state('void', style({ opacity: 0 })),
      state('*', style({ opacity: 1 })),
      transition('void <=> *', animate('200ms ease-in-out')),
    ]),
    trigger('detailsFadeInOut', [
      state('void', style({ opacity: 0, transform: 'translate(-50%, -50%) scale(0.9)' })),
      state('*', style({ opacity: 1, transform: 'translate(-50%, -50%) scale(1)' })),
      transition('void <=> *', animate('200ms ease-in-out')),
    ])
  ]
})
export class UserEventsComponent implements OnInit {
  userId!: number;
  events: UserEventDTO[] = [];
  eventDetails!: EventDTO;
  eventToEdit!: EventDTO;

  userBands: Band[] = [];
  bookings: Booking[] = [];
  userVenues: Venue[] = [];
  showForm: boolean = false;
  editingMode: boolean = false;
  isDetailsVisible = false;

  constructor(private eventApiService: EventApiService, private bookingApiService: BookingApiService, private musicianApiService: MusicianApiService, private venueApiService: VenueApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.eventApiService.getUserEvents(this.userId).subscribe(events => {
      if(events) {
        this.events = [...events];
      }
    });

    this.musicianApiService.getMusicianBands(this.userId).subscribe(bands => {
      if(bands) {
        this.userBands = bands.filter(b => b.founderId === this.userId);
      }
    });

    this.bookingApiService.getUsableUserBookings(this.userId).subscribe(bookings => {
      if(bookings) {
        this.bookings = bookings.filter(b => !this.events.some(e => e.bookingId === b.id));
      }
    });

    this.venueApiService.getUserVenues(this.userId).subscribe(venues => {
      if(venues) {
        this.userVenues = venues;
      }
    });
  }

  showDetails(id: number) {
    this.eventApiService.getEvent(id).subscribe(event => {
      if(event) {
        this.eventDetails = event;
      }
    });
    this.isDetailsVisible = true;
  }

  closeDetails() {
    this.isDetailsVisible = false;
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  turnEditingMode(event: EventDTO) {
    this.closeDetails()
    this.editingMode = true;
    this.eventToEdit = event;
  }

  handleEventRegistration(event: EventRequestDTO) {
    this.eventApiService.createEvent(event).subscribe(event => {
      if(event) {
        this.events.push(event);
        this.showForm = false;
      }
    });
  }

  handleEventUpdating(event: EventUpdateRequestDTO) {
    this.eventApiService.updateEvent(event).subscribe(event => {
      if (event) {
        this.editingMode = false;
        this.events = this.events.map(e => e.id === event.id ? event : e);
      }
    });
  }
}
