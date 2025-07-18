import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import {OfferRequest, OfferStatus} from '../../core/models/offer.model';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {Venue} from '../../core/models/venue.model';
import {CarouselSelectorComponent} from '../carousel-selector/carousel-selector.component';

@Component({
  selector: 'app-offer-input',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    CarouselSelectorComponent
  ],
  templateUrl: './offer-input.component.html',
  styleUrl: './offer-input.component.css'
})
export class OfferInputComponent implements OnInit, OnChanges {

  @Input() userId!: number;
  @Input() inputOffer!: OfferRequest;
  @Input() venues!: Venue[];
  @Output() outputOffer = new EventEmitter<OfferRequest>();

  offer!: OfferRequest;
  statuses: OfferStatus[] = Object.values(OfferStatus) as OfferStatus[];

  venues2 = [
    { id: 1, name: 'Studio A' },
    { id: 2, name: 'Sala prób B' },
    { id: 3, name: 'Rock Spot C' },
    { id: 4, name: 'Jazz Room D' },
    { id: 4, name: 'Jazz Room D' },
    { id: 4, name: 'Jazz Room D' },
    { id: 4, name: 'Jazz Room D' },
  ];

  statusLabels: Record<OfferStatus, string> = {
    [OfferStatus.ACTIVE]: 'Aktywna',
    [OfferStatus.CLAIMED]: 'Zajęta',
    [OfferStatus.CANCELED]: 'Anulowana'
  };

  ngOnInit() {
    this.initVenue();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputOffer'] && changes['inputOffer'].currentValue) {
      this.initVenue();
    }
  }

  getStatusLabel(status: OfferStatus): string {
    return this.statusLabels[status] ?? status;
  }

  selectVenue(venueId: number) {
    this.offer.venueId = venueId;
    this.emit();
  }

  private initVenue() {
    this.offer = this.inputOffer ?? {
      venueId: 0,
      pricePerHour: 0,
      date: '',
      startTime: '',
      endTime: '',
      status: OfferStatus.ACTIVE
    };
  }

  emit() {
    this.outputOffer.emit(this.offer);
  }
}
