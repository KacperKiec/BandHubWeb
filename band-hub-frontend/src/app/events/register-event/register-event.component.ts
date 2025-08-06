import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Booking} from '../../core/models/booking.model';
import {EventRequestDTO} from '../../core/models/event.model';
import {Band} from '../../core/models/band.model';
import {EventInputComponent} from '../event-input/event-input.component';
import {Venue} from '../../core/models/venue.model';

@Component({
  selector: 'app-register-event',
  standalone: true,
  imports: [
    EventInputComponent
  ],
  templateUrl: './register-event.component.html',
  styleUrl: './register-event.component.css'
})
export class RegisterEventComponent  implements OnInit {
  @Input() userId!: number;
  @Input() bookings: Booking[] = [];
  @Input() userBands: Band[] = [];
  @Input() venues: Venue[] = [];
  @Output() registered = new EventEmitter<EventRequestDTO>();

  event!: EventRequestDTO;

  ngOnInit() {
    this.event = {
      creatorId: this.userId,
      name: '',
      bandId: 0,
      bookingId: 0,
      venueId: 0,
      date: '',
      startTime: '',
      endTime: '',
      description: '',
      ticketPrice: 0
    }
  }

  getEvent(value: EventRequestDTO) {
    this.event = {
      creatorId: this.userId,
      name: value.name,
      bandId: value.bandId,
      bookingId: value.bookingId,
      venueId: value.venueId,
      date: value.date,
      startTime: value.startTime,
      endTime: value.endTime,
      description: value.description,
      ticketPrice: value.ticketPrice
    }
  }

  submit() {
    this.registered.emit(this.event);
  }
}
