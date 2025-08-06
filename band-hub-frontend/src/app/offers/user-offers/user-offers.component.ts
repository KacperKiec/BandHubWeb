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
import {DecimalPipe, NgForOf, NgIf} from '@angular/common';
import {Venue} from '../../core/models/venue.model';
import {RegisterOfferComponent} from '../register-offer/register-offer.component';
import {Offer, OfferRequest, OfferStatus, OfferUpdateRequest} from '../../core/models/offer.model';
import {OfferApiService} from '../offer-service/offer-api.service';
import {EditOfferFormComponent} from '../edit-offer-form/edit-offer-form.component';

@Component({
  selector: 'app-user-offers',
  standalone: true,
  imports: [
    NgIf,
    RegisterOfferComponent,
    NgForOf,
    EditOfferFormComponent,
    DecimalPipe
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
  offersToDisplay: Offer[] = [];
  activeTab: string = 'active';

  constructor(private cdr: ChangeDetectorRef, private offerApiService: OfferApiService) {
  }

  ngOnInit() {
    this.checkCollectionsState();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ((changes['venues'] || changes['offers'])) {
      this.checkCollectionsState();

      if (this.offers && this.offers.length > 0) {
        this.filterOffers([OfferStatus.ACTIVE]);
      }

      this.cdr.detectChanges();
    }
  }

  private checkCollectionsState() {
    this.hasVenues = this.venues && this.venues.length > 0;
    this.hasOffers = this.offers && this.offers.length > 0;
  }

  getOfferStatus(status: OfferStatus) {
    switch(status) {
      case OfferStatus.ACTIVE: return "AKTYWNA";
      case OfferStatus.CLAIMED: return "ZAJĘTA";
      case OfferStatus.CANCELED: return "ANULOWANA";
      case OfferStatus.ENDED: return "ZAKOŃCZONA";
    }
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  turnEditingMode(offer: Offer) {
    this.editingMode = true;
    this.offerToEdit = offer;
  }

  addAnotherOffer() {
    this.showForm = true;
    this.hasOffers = false;
  }

  handleOfferRegistration(offer: OfferRequest) {
    this.offerApiService.addOffer(offer).subscribe(offer => {
      if (offer) {
        this.showForm = false;
        this.updateData.emit(true);
      }
    });
  }

  handleOfferUpdating(offer: OfferUpdateRequest) {
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
      }
    });
  }

  filterOffers(statuses: OfferStatus[]) {
    this.offersToDisplay = this.offers.filter(offer => statuses.includes(offer.status));
  }

  protected readonly OfferStatus = OfferStatus;
}
