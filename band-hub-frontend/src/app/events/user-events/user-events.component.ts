import { Component } from '@angular/core';
import {CalendarComponent} from '../../shared/calendar/calendar.component';

@Component({
  selector: 'app-user-events',
  standalone: true,
  imports: [
    CalendarComponent
  ],
  templateUrl: './user-events.component.html',
  styleUrl: './user-events.component.css'
})
export class UserEventsComponent {

}
