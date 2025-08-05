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
import {RelationType, UserEventDTO} from '../../core/models/event.model';
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
  @Input() userEvents: UserEventDTO[] = [];

  eventsToDisplay: CalendarEvent[] = [];
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();

  events: CalendarEvent[] = [
    {
      id: 1,
      start: new Date(),
      end: new Date(),
      title: 'Moje wydarzenie',
      color: { primary: '#1e90ff', secondary: '#D1E8FF' },
    },
    {
      id: 2,
      start: new Date(),
      end: new Date(),
      title: 'Moje wydarzenie 2',
      color: { primary: '#1e90ff', secondary: '#D1E8FF' },
    },
    {
      id: 1,
      start: new Date(),
      end: new Date(),
      title: 'Moje wydarzenie3',
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
            title: event.eventName,
            color: this.getColorByRelation(event.relation),
            tooltip: this.getTooltipByRelation(event.relation)
          };
        });
      }

      this.cdr.detectChanges();
    }
  }

  getColorByRelation(relation: RelationType): { primary: string; secondary: string } {
    switch (relation) {
      case RelationType.LANDLORD:
        return { primary: '#ffc107', secondary: '#fff3cd' };
      case RelationType.PARTICIPANT:
        return { primary: '#28a745', secondary: '#d4edda' };
      case RelationType.FOLLOWER:
        return { primary: '#17a2b8', secondary: '#d1ecf1' };
      default:
        return { primary: '#6c757d', secondary: '#e2e3e5' };
    }
  }

  getTooltipByRelation(relation: RelationType): string {
    switch (relation) {
      case RelationType.LANDLORD:
        return 'Wydarzenie w lokalu, który wynajmujesz';
      case RelationType.PARTICIPANT:
        return 'Wydarzenie utworzone przez Ciebie';
      case RelationType.FOLLOWER:
        return 'Obserwowane wydarzenie';
      default:
        return 'Wydarzenie';
    }
  }

  showDetails(id: number) {
    console.log(id);
  }
}
