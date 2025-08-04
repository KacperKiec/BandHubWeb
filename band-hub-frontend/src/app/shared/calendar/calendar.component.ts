import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  LOCALE_ID,
  OnChanges,
  SimpleChanges
} from '@angular/core';
import {
  CalendarA11y,
  CalendarDateFormatter,
  CalendarEvent,
  CalendarEventTitleFormatter,
  CalendarModule,
  CalendarUtils,
  CalendarView,
  DateAdapter
} from 'angular-calendar';
import {adapterFactory} from 'angular-calendar/date-adapters/date-fns';
import {Subject} from 'rxjs';
import {EventDTO} from '../../core/models/event.model';
import {DatePipe, NgForOf} from '@angular/common';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [
    CalendarModule,
    NgForOf,
    DatePipe
  ],
  providers: [
    {
      provide: DateAdapter,
      useFactory: adapterFactory,
    },
    { provide: LOCALE_ID, useValue: 'pl' },
    CalendarUtils,
    CalendarA11y,
    CalendarDateFormatter,
    CalendarEventTitleFormatter
  ],
  templateUrl: './calendar.component.html',
  styleUrl: './calendar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CalendarComponent implements OnChanges {
  @Input() userEvents: EventDTO[] = [];

  eventsToDisplay: CalendarEvent[] = [];

  view: CalendarView = CalendarView.Month;
  CalendarView = CalendarView;

  viewDate: Date = new Date();
  refresh: Subject<any> = new Subject();

  events: CalendarEvent[] = [
    {
      id: 1,
      start: new Date(),
      end: new Date(),
      title: 'Moje wydarzenie',
      color: { primary: '#1e90ff', secondary: '#D1E8FF' },
    },
  ];
  constructor(private cdr: ChangeDetectorRef) { }


  ngOnChanges(changes: SimpleChanges) {
    if (changes['userEvents']) {

      if (this.userEvents && this.userEvents.length > 0) {
        this.eventsToDisplay = this.userEvents.map(event => {
          const start = new Date(`${event.date}T${event.startTime}`);
          const end = new Date(`${event.date}T${event.endTime}`);

          return {
            id: event.id,
            start,
            end,
            title: event.name,
            color: { primary: '#1e90ff', secondary: '#D1E8FF' }
          };
        });
      }

      this.cdr.detectChanges();
    }
  }

  eventClicked({ event }: { event: CalendarEvent }): void {
    alert(`Kliknięto wydarzenie: ${event.title}`);
  }

  showDetails(id: number) {
    console.log(id);
  }
}
