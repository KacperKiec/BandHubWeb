import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Venue} from '../../core/models/venue.model';
import {EventDTO, EventRequestDTO, EventUpdateRequestDTO} from '../../core/models/event.model';
import {Booking} from '../../core/models/booking.model';
import {Band} from '../../core/models/band.model';
import {EventInputComponent} from '../event-input/event-input.component';

@Component({
  selector: 'app-edit-event-form',
  standalone: true,
  imports: [
    FormsModule,
    EventInputComponent
  ],
  templateUrl: './edit-event-form.component.html',
  styleUrl: './edit-event-form.component.css'
})
export class EditEventFormComponent implements OnInit {
  @Input() userId!: number;
  @Input() bookings: Booking[] = [];
  @Input() userBands: Band[] = [];
  @Input() venues: Venue[] = [];
  @Input() eventTemplate!: EventDTO;
  @Output() updated = new EventEmitter<EventUpdateRequestDTO>();

  event!: EventUpdateRequestDTO;

  ngOnInit() {
    if(this.eventTemplate) {
      this.event = {
        id: this.eventTemplate.id,
        creatorId: this.userId,
        name: this.eventTemplate.name,
        bandId: this.eventTemplate.bandId,
        bookingId: this.eventTemplate.bookingId,
        venueId: this.eventTemplate.venueId,
        date: this.eventTemplate.date,
        startTime: this.eventTemplate.startTime,
        endTime: this.eventTemplate.endTime,
        description: this.eventTemplate.description,
        ticketPrice: this.eventTemplate.ticketPrice
      };
    }
  }

  getEvent(value: EventRequestDTO) {
    this.event = {
      id: this.eventTemplate.id,
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
    };
  }

  submitForm() {
    this.updated.emit(this.event);
  }
}
