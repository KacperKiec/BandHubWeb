import {Component, OnInit} from '@angular/core';
import {NgSwitch, NgSwitchCase} from "@angular/common";
import {UserVenuesComponent} from '../../venues/user-venues/user-venues.component';
import {UserOffersComponent} from '../../offers/user-offers/user-offers.component';
import {VenueApiService} from '../../venues/venue-service/venue-api.service';
import {Venue} from '../../core/models/venue.model';
import {Offer} from '../../core/models/offer.model';
import {OfferApiService} from '../../offers/offer-service/offer-api.service';
import {OwnerBookingsComponent} from '../../bookings/owner-bookings/owner-bookings.component';
import {BookingApiService} from '../../bookings/booking-service/booking-api.service';
import {Booking} from '../../core/models/booking.model';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-renting-panel',
  standalone: true,
  imports: [
    UserVenuesComponent,
    UserOffersComponent,
    NgSwitch,
    NgSwitchCase,
    OwnerBookingsComponent
  ],
  templateUrl: './renting-panel.component.html',
  styleUrl: './renting-panel.component.css'
})
export class RentingPanelComponent implements OnInit {

  activeTab: string = 'venues';
  userId!: number;
  venues: Venue[] = [];
  offers: Offer[] = [];
  bookings: Booking[] = [];

  currentPageB = 0;
  pageSizeB = 10;
  totalPagesB = 0;

  constructor(private venueApiService: VenueApiService, private offerApiService: OfferApiService, private bookingApiService: BookingApiService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.initData()
    this.route.queryParams.subscribe(params => {
      this.activeTab = params['mode'] ?? 'venues';
    });

  }

  initData() {
    this.venueApiService.getUserVenues(this.userId).subscribe(venues => {
      if (venues) {
        this.venues = [...venues];
      }
    });

    this.offerApiService.getUserOffers(this.userId).subscribe(offers => {
      if (offers) {
        this.offers = [...offers];
      }
    });

    this.loadBookingPage(0);
  }

  loadBookingPage(page: number) {
    this.bookingApiService.getOwnerBookings(this.userId, page, this.pageSizeB).subscribe({
      next: (response) => {
        this.totalPagesB = response.page.totalPages;
        this.currentPageB = response.page.number;
        this.bookings = [...response.content];
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }

  updateData($event: boolean) {
    if ($event) this.initData();
  }
}
