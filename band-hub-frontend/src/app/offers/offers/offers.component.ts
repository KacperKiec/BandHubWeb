import {Component, OnInit} from '@angular/core';
import {MapComponent} from '../../shared/map/map.component';
import {CoordinatesRanges, MapPoint, TimeRange} from '../../core/models/shared.model';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {OfferApiService} from '../offer-service/offer-api.service';
import {DatePickerComponent} from '../../shared/date-picker/date-picker.component';
import {Offer, OfferSearchingParams, OfferStatus} from '../../core/models/offer.model';
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BookingRequest} from '../../core/models/booking.model';
import {BookingApiService} from '../../bookings/booking-service/booking-api.service';

@Component({
  selector: 'app-offers',
  standalone: true,
  imports: [
    MapComponent,
    DatePickerComponent,
    NgIf,
    NgForOf,
    FormsModule,
    DecimalPipe
  ],
  templateUrl: './offers.component.html',
  styleUrl: './offers.component.css',
  animations: [
    trigger('expandCollapse', [
      state('visible', style({
        height: '*',
        opacity: 1,
        padding: '*',
        margin: '*',
        overflow: 'hidden'
      })),
      state('hidden', style({
        height: '0px',
        opacity: 0,
        padding: '0px',
        margin: '0px',
        overflow: 'hidden'
      })),
      transition('hidden => visible', [
        animate('300ms ease-out')
      ]),
      transition('visible => hidden', [
        animate('300ms ease-in')
      ]),
    ])
  ]
})
export class OffersComponent implements OnInit {
  userId!: number;
  offers!: Offer[];
  mapPoints: MapPoint[] = [];
  isVisible: boolean = false;
  searchingDate: string = new Date().toISOString().substring(0, 10);
  searchingCoords: CoordinatesRanges = {
    minLatitude: 50.0,
    maxLatitude: 50.2,
    minLongitude: 19.9,
    maxLongitude: 20.1
  };
  params!: OfferSearchingParams;

  timeOptions: string[] = [];
  selectedOffers: Offer[] = [];
  booking!:BookingRequest;
  canSubmit: boolean = true;
  submitDisableReason: string = "";

  constructor(private offerApiService: OfferApiService, private bookingApiService: BookingApiService) {
  }

  ngOnInit() {
    this.timeOptions = this.generateTimeOptions();
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    } else {
      this.canSubmit = false;
      this.submitDisableReason = "Przed dokonaniem rezerwacji musisz się zalogować";
    }

    this.updateParamsAndFetchOffers();
  }

  getCoords(coords: CoordinatesRanges) {
    this.searchingCoords = coords;
    this.updateParamsAndFetchOffers();
  }

  getDate(date: string) {
    this.searchingDate = date;
    this.updateParamsAndFetchOffers();
  }

  updateParamsAndFetchOffers() {
    this.params = {
      cities: [],
      status: OfferStatus.ACTIVE,
      date: this.searchingDate,
      minLatitude: this.searchingCoords.minLatitude,
      maxLatitude: this.searchingCoords.maxLatitude,
      minLongitude: this.searchingCoords.minLongitude,
      maxLongitude: this.searchingCoords.maxLongitude,
    };
    this.getOffers();
  }

  showDetails(value: {lat: number, lng: number}) {
    this.selectedOffers = [];
    this.isVisible = true;

    this.offers.forEach(offer => {
      if(offer.latitude === value.lat && offer.longitude === value.lng) {
        this.selectedOffers.push(offer);
      }
    });

    this.selectedOffers.sort((a, b) => a.startTime.localeCompare(b.startTime));

    this.selectedOffers.forEach(offer => {
      offer.availableRanges.sort((a, b) => a.startTime.localeCompare(b.startTime));
    });
  }

  prepareBooking(offer: Offer, range: TimeRange) {
    this.booking = {
      tenantId: this.userId,
      offerId: offer.id,
      startTime: range.startTime,
      endTime: range.endTime,
      bookingDate: new Date().toISOString().substring(0, 10)
    }
  }

  closeDetails() {
    this.isVisible = false;

    if (this.booking) {
      this.booking.startTime = '';
      this.booking.endTime = '';
      this.booking.offerId = 0;
    }
  }

  getShortTime(time: string) {
    return time.substring(0, 5);
  }

  getOffers() {
    this.offerApiService.getOffers(this.params).subscribe(offers => {
      if (offers) {
        this.offers = offers;
        this.mapPoints = this.offers.map(offer => ({
          id: offer.id,
          name: offer.venueName,
          latitude: offer.latitude,
          longitude: offer.longitude
        }));
      }
    });
  }

  getOfferStatus(status: OfferStatus) {
    switch(status) {
      case OfferStatus.ACTIVE: return "AKTYWNA";
      case OfferStatus.CLAIMED: return "ZAJĘTA";
      case OfferStatus.CANCELED: return "ANULOWANA";
      case OfferStatus.ENDED: return "ZAKOŃCZONA";
    }
  }

  confirmBooking() {
    console.log(this.booking);
    this.bookingApiService.createBooking(this.booking).subscribe({
      next: () => {
        alert('Dokonano rezerwacji');
        this.closeDetails();
        this.updateParamsAndFetchOffers();
      }
    });
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
}
