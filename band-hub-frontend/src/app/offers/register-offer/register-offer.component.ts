import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {OfferRequest, OfferStatus} from '../../core/models/offer.model';
import {OfferInputComponent} from '../../shared/offer-input/offer-input.component';
import {Venue} from '../../core/models/venue.model';

@Component({
  selector: 'app-register-offer',
  standalone: true,
  imports: [
    OfferInputComponent
  ],
  templateUrl: './register-offer.component.html',
  styleUrl: './register-offer.component.css'
})
export class RegisterOfferComponent implements OnInit {
  @Input() userId!: number;
  @Input() venues!: Venue[];
  @Output() registered = new EventEmitter<OfferRequest>();

  offer!: OfferRequest;

  ngOnInit() {
    this.offer = {
      venueId: 0,
      pricePerHour: 0,
      date: '',
      startTime: '',
      endTime: '',
      status: OfferStatus.ACTIVE
    }
  }

  getOffer(value: OfferRequest) {
    this.offer = {
      venueId: value.venueId,
      pricePerHour: value.pricePerHour,
      date: value.date,
      startTime: value.startTime,
      endTime: value.endTime,
      status: value.status
    }
  }

  submit() {
    console.log('Oferta dodana' + this.offer);
    this.registered.emit(this.offer);
  }
}
