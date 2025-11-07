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
import {Venue} from '../../core/models/venue.model';
import {EventRequestDTO} from '../../core/models/event.model';
import {Booking} from '../../core/models/booking.model';
import {Band} from '../../core/models/band.model';
import {CarouselSelectorComponent} from '../../shared/carousel-selector/carousel-selector.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf, NgSwitch, NgSwitchCase} from '@angular/common';
import {CarouselElement} from '../../core/models/shared.model';

enum VenueSelectionMode {
  BOOKING = 'BOOKING',
  OWNED = 'OWNED'
}

@Component({
  selector: 'app-event-input',
  standalone: true,
  imports: [
    CarouselSelectorComponent,
    FormsModule,
    ReactiveFormsModule,
    NgSwitch,
    NgSwitchCase,
    NgIf,
    NgForOf
  ],
  templateUrl: './event-input.component.html',
  styleUrl: './event-input.component.css',
})
export class EventInputComponent implements OnInit, OnChanges {

  @Input() userId!: number;
  @Input() inputEvent!: EventRequestDTO;
  @Input() bookings!: Booking[];
  @Input() userBands!: Band[];
  @Input() venues!: Venue[];
  @Output() outputEvent = new EventEmitter<EventRequestDTO>();

  timeOptions: string[] = [];
  event!: EventRequestDTO;
  venueSelectionMode: VenueSelectionMode = VenueSelectionMode.BOOKING;


  constructor(private cdr: ChangeDetectorRef) {
    this.timeOptions = this.generateTimeOptions();
    this.initEvent();
  }

  ngOnInit() {
    this.initEvent();
    console.log('Rezerwacje uzytkownika: ' + this.bookings);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputEvent'] && changes['inputEvent'].currentValue) {
      this.initEvent();
    }

    if (changes['bookings'] || changes['userBands'] || changes['venues']) {
      this.cdr.detectChanges();
      console.log('Rezerwacje uzytkownika: ' + this.bookings);
    }
  }

  selectBooking(bookingId: number) {
    const booking = this.bookings.find(b => b.id === bookingId);
    this.event.bookingId = bookingId;
    this.event.date = <string>booking?.bookedDate
    this.event.startTime = <string>booking?.startTime
    this.event.endTime = <string>booking?.endTime
    this.event.venueId = <number>booking?.venueId;
    this.emit();
  }

  selectBand(bandId: number) {
    this.event.bandId= bandId;
    this.emit();
  }

  selectVenue(venueId: number) {
    this.event.venueId = venueId;
    this.emit();
  }

  get mappedBookings(): CarouselElement[] {
    return this.bookings.map(booking => ({
      id: booking.id,
      name: `${booking.venueName} (${booking.bookedDate})`
    }));
  }

  get isEditMode(): boolean {
    return !!this.inputEvent;
  }

  get isBookingEvent(): boolean {
    return this.event.bookingId !== 0;
  }

  private initEvent() {
    if (this.isEditMode && this.inputEvent.bookingId === null) {
      this.venueSelectionMode = VenueSelectionMode.OWNED
    }

    this.event = this.inputEvent ?? {
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
    };
  }

  setVenueMode(mode: VenueSelectionMode) {
    this.venueSelectionMode = mode;

    if (mode === VenueSelectionMode.BOOKING) {
      this.event.venueId = <number>this.bookings.find(b => b.id === this.event.bookingId)?.venueId;
    } else {
      this.event.bookingId = 0;
    }

    this.emit();
  }

  private generateTimeOptions(): string[] {
    const options: string[] = [];
    for (let h = 0; h < 24; h++) {
      for (let m = 0; m < 60; m += 30) {
        const hh = String(h).padStart(2, '0');
        const mm = String(m).padStart(2, '0');
        options.push(`${hh}:${mm}`);
      }
    }
    return options;
  }

  emit() {
    this.outputEvent.emit(this.event);
  }

  protected readonly VenueSelectionMode = VenueSelectionMode;
}
