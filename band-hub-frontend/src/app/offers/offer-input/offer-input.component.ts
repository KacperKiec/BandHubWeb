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
import {OfferRequest, OfferStatus} from '../../core/models/offer.model';
import {FormsModule} from '@angular/forms';
import {NgForOf} from '@angular/common';
import {Venue} from '../../core/models/venue.model';
import {CarouselSelectorComponent} from '../../shared/carousel-selector/carousel-selector.component';

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

  timeOptions: string[] = [];
  statuses: OfferStatus[] = [];
  statusLabels: Record<OfferStatus, string> = {
    [OfferStatus.ACTIVE]: 'Aktywna',
    [OfferStatus.CLAIMED]: 'Zajęta',
    [OfferStatus.CANCELED]: 'Anulowana',
    [OfferStatus.ENDED]: 'Zakoczona'
  };

  constructor(private cdr: ChangeDetectorRef) {
    this.statuses = Object.values(OfferStatus) as OfferStatus[];
    this.initOffer();
  }

  ngOnInit() {
    this.timeOptions = this.generateTimeOptions();
    this.initOffer();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputOffer'] && changes['inputOffer'].currentValue) {
      this.initOffer();
    }

    if (changes['venues']) {
      this.cdr.detectChanges();
    }
  }

  getStatusLabel(status: OfferStatus): string {
    return this.statusLabels[status] ?? status;
  }

  selectVenue(venueId: number) {
    this.offer.venueId = venueId;
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

  private initOffer() {
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
