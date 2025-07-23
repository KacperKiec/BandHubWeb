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
import {NgIf} from '@angular/common';
import {Venue} from '../../core/models/venue.model';
import {RegisterOfferComponent} from '../register-offer/register-offer.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {Offer, OfferRequest, OfferUpdateRequest} from '../../core/models/offer.model';
import {OfferApiService} from '../offer-service/offer-api.service';

@Component({
  selector: 'app-user-offers',
  standalone: true,
  imports: [
    NgIf,
    RegisterOfferComponent,
    RegisterVenueComponent
  ],
  templateUrl: './user-offers.component.html',
  styleUrl: './user-offers.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserOffersComponent implements OnInit, OnChanges {
  @Input() venues!: Venue[];
  @Input() offers!: Offer[];
  @Input() userId!: number;
  @Output() updateData = new EventEmitter<boolean>();

  hasVenues: boolean = false;
  hasOffers: boolean = false;
  showForm: boolean = false;
  editingMode: boolean = false;

  offerToEdit!: Offer;

  constructor(private cdr: ChangeDetectorRef, private offerApiService: OfferApiService) {
  }

  ngOnInit() {
    this.checkCollectionsState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['venues'] || changes['offers']) {
      this.checkCollectionsState();
      this.cdr.detectChanges();
    }
  }

  private checkCollectionsState() {
    this.hasVenues = this.venues && this.venues.length > 0;
    this.hasOffers = this.offers && this.offers.length > 0;
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  turnEditingMode(offer: Offer) {
    this.editingMode = true;
    this.offerToEdit = offer;
    console.log(this.offerToEdit);
  }

  addAnotherOffer() {
    this.showForm = true;
    this.hasOffers = false;
  }

  handleOfferRegistration(offer: OfferRequest) {
    console.log(offer);
    this.offerApiService.addOffer(offer).subscribe(offer => {
      if (offer) {
        this.showForm = false;
        this.updateData.emit(true);
      }
    });
  }

  handleOfferUpdating(offer: OfferUpdateRequest) {
    console.log(offer);
    this.offerApiService.updateOffer(offer).subscribe(offer => {
      if (offer) {
        this.editingMode = false;
        this.updateData.emit(true);
      }
    });
  }

  deleteOffer(id: number) {
    this.offerApiService.deleteOffer(id).subscribe({
      next: () => {
        this.updateData.emit(true);
      },
      error: err => {
        console.error('Blad podczas usuwania oferty: ', err);
      }
    });
  }
}
