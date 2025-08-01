import {Component, OnInit} from '@angular/core';
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Booking} from '../../core/models/booking.model';
import {BookingApiService} from '../booking-service/booking-api.service';

@Component({
  selector: 'app-user-bookings',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    DecimalPipe
  ],
  templateUrl: './user-bookings.component.html',
  styleUrl: './user-bookings.component.css'
})
export class UserBookingsComponent implements OnInit {
  bookings!: Booking[];
  userId!: number;

  currentPage = 0;
  totalPages = 0;
  pageSize = 10;

  hasBookings: boolean = false;

  constructor(private bookingApiService: BookingApiService) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.loadPage(this.currentPage);
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.loadPage(page)
    }
  }

  deleteBooking(id: number) {
    this.bookingApiService.deleteBooking(id, 'tenant').subscribe({
      next: () => {
        this.loadPage(this.currentPage);
      }
    });
  }

  loadPage(page: number) {
    this.bookingApiService.getUserBookings(this.userId, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.bookings = [...response.content];
        if (this.bookings.length > 0) this.hasBookings = true;
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }
}
