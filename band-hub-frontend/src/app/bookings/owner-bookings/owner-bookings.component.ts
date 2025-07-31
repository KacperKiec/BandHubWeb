import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {Offer} from '../../core/models/offer.model';
import {Booking} from '../../core/models/booking.model';
import {NgForOf, NgIf} from '@angular/common';
import {BookingApiService} from '../booking-service/booking-api.service';

@Component({
  selector: 'app-owner-bookings',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './owner-bookings.component.html',
  styleUrl: './owner-bookings.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OwnerBookingsComponent implements OnInit, OnChanges{
  @Input() offers!: Offer[];
  @Input() bookings!: Booking[];
  @Input() userId!: number;
  @Output() updateData = new EventEmitter<boolean>();

  @Input() currentPage = 0;
  @Input() totalPages = 0;
  @Output() pageChange = new EventEmitter<number>();

  hasOffers: boolean = false;
  hasBookings: boolean = false;

  constructor(private cdr: ChangeDetectorRef, private bookingApiService: BookingApiService) {}

  ngOnInit() {
    this.checkCollectionsState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['bookings'] || changes['offers'])) {
      this.checkCollectionsState();

      this.cdr.detectChanges();
    }
  }

  private checkCollectionsState() {
    this.hasBookings = this.bookings && this.bookings.length > 0;
    this.hasOffers = this.offers && this.offers.length > 0;
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.pageChange.emit(page);
    }
  }

  getTenantName(id: number) {
    const booking = this.bookings.find(booking => booking.id === id);
    return booking?.tenantFirstName + ' ' + booking?.tenantLastName;
  }

  deleteBooking(id: number) {
    this.bookingApiService.deleteBooking(id, 'owner').subscribe({
      next: () => {
        this.updateData.emit(true);
      }
    });
  }
}
