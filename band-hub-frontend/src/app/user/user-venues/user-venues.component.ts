import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges
} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';
import {RegisterVenueComponent} from '../../venues/register-venue/register-venue.component';
import {Venue, VenueRequest, VenueUpdateRequest} from '../../core/models/venue.model';
import {VenueApiService} from '../../services/venue-service/venue-api.service';
import {EditBandComponent} from '../edit/edit-band/edit-band.component';
import {EditVenueFormComponent} from '../edit/edit-venue-form/edit-venue-form.component';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-user-venues',
  standalone: true,
  imports: [
    NgIf,
    RegisterBandComponent,
    RegisterVenueComponent,
    NgForOf,
    EditBandComponent,
    EditVenueFormComponent
  ],
  templateUrl: './user-venues.component.html',
  styleUrl: './user-venues.component.css',
  animations: [
    trigger('expandCollapse', [
      state('collapsed', style({
        height: '0px',
        overflow: 'hidden',
        opacity: 0
      })),
      state('expanded', style({
        height: '*',
        overflow: 'hidden',
        opacity: 1
      })),
      transition('collapsed <=> expanded', [
        animate('300ms ease-in-out')
      ]),
    ])
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserVenuesComponent implements OnChanges {

  @Input() userId!: number;
  @Input() venues!:Venue[];
  @Output() updateData = new EventEmitter<boolean>();

  hasVenues: boolean = false;
  showForm: boolean = false;
  editingMode: boolean = false;

  venueToEdit!: Venue;
  expandedVenueId: number | null = null;

  constructor(private venueApiService: VenueApiService, private cdr: ChangeDetectorRef) {
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['venues']) {
      this.hasVenues = this.venues && this.venues.length > 0;
    }

    this.cdr.markForCheck();
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  addAnotherVenue() {
    this.showForm = true;
    this.hasVenues = false;
  }

  viewVenueDescription(venueId: number) {
    this.expandedVenueId = this.expandedVenueId === venueId ? null : venueId;
  }

  goToVenues() {

  }

  deleteVenue(id: number) {
    this.venueApiService.deleteVenue(id).subscribe({
      next: () => {
        this.updateData.emit(true);
      },
      error: (err) => {
        console.error('Błąd podczas usuwania lokalu:', err);
      }
    });
  }

  turnEditingMode(venue: Venue) {
    this.editingMode = true;
    this.venueToEdit = venue;
    console.log(this.venueToEdit);
  }

  handleVenueRegistration(venue: VenueRequest) {
    this.venueApiService.addVenue(venue).subscribe(venue => {
      if (venue) {
        this.showForm = false;
        this.updateData.emit(true);
      }
    });
  }

  handleVenueUpdating(venue: VenueUpdateRequest) {
    this.venueApiService.updateVenue(venue).subscribe(venue => {
      if (venue) {
        this.editingMode = false;
        this.updateData.emit(true);
      }
    });
  }
}
