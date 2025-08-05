import {Component, OnInit} from '@angular/core';
import {CalendarComponent} from '../../shared/calendar/calendar.component';
import {UserEventDTO} from '../../core/models/event.model';

@Component({
  selector: 'app-user-events',
  standalone: true,
  imports: [
    CalendarComponent
  ],
  templateUrl: './user-events.component.html',
  styleUrl: './user-events.component.css'
})
export class UserEventsComponent implements OnInit {
  events: UserEventDTO[] = [];

  constructor() {
  }

  ngOnInit() {

  }
}
