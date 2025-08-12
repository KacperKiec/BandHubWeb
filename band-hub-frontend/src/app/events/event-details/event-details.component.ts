import {Component, EventEmitter, Input, Output} from '@angular/core';
import {EventDTO} from '../../core/models/event.model';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.css'
})
export class EventDetailsComponent {
  @Input() userId!: number;
  @Input() eventDetails!: EventDTO;
  @Output() closeDetailsEvent = new EventEmitter<void>();
  @Output() turnEditingModeEvent = new EventEmitter<EventDTO>();

  closeDetails() {
    this.closeDetailsEvent.emit();
  }

  turnEditingMode(event: EventDTO) {
    this.turnEditingModeEvent.emit(event);
  }
}
