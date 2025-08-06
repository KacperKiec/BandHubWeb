import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Offer, OfferRequest, OfferUpdateRequest} from '../../core/models/offer.model';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {OfferInputComponent} from '../offer-input/offer-input.component';
import {Venue} from '../../core/models/venue.model';

@Component({
  selector: 'app-edit-offer-form',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    OfferInputComponent
  ],
  templateUrl: './edit-offer-form.component.html',
  styleUrl: './edit-offer-form.component.css'
})
export class EditOfferFormComponent implements OnInit {
  @Input() userId!: number;
  @Input() offerTemplate!: Offer;
  @Input() venues!: Venue[];
  @Output() updated: EventEmitter<OfferUpdateRequest> = new EventEmitter();

  offer!:OfferUpdateRequest;

  ngOnInit() {
    if(this.offerTemplate) {
      this.offer = {
        id: this.offerTemplate.id,
        venueId: this.offerTemplate.venueId,
        pricePerHour: this.offerTemplate.pricePerHour,
        date: this.offerTemplate.date,
        startTime: this.offerTemplate.startTime,
        endTime: this.offerTemplate.endTime,
        status: this.offerTemplate.status
      };
    }
  }

  getOffer(value: OfferRequest) {
    this.offer = {
      id: this.offerTemplate.id,
      venueId: value.venueId,
      pricePerHour: value.pricePerHour,
      date: value.date,
      startTime: value.startTime,
      endTime: value.endTime,
      status: value.status
    };
  }

  submitForm() {
    this.updated.emit(this.offer);
  }
}
