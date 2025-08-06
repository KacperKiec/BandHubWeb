import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component, EventEmitter,
  Input,
  LOCALE_ID,
  OnChanges, Output,
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
  @Output() getDetails = new EventEmitter<number>();

  eventsToDisplay: CalendarEvent[] = [];
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();

  constructor(private cdr: ChangeDetectorRef) { }


  ngOnChanges(changes: SimpleChanges) {
    if (changes['userEvents']) {

      if (this.userEvents && this.userEvents.length > 0) {
        this.eventsToDisplay = this.userEvents.map(event => {
          const start = new Date(`${event.date}T${event.startTime}`);
          const end = new Date(`${event.date}T${event.endTime}`);

          return {
            id: event.eventId,
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
        return 'Wydarzenie, w którym bierzesz czynny udział';
      case RelationType.FOLLOWER:
        return 'Obserwowane wydarzenie';
      default:
        return 'Wydarzenie';
    }
  }

  showDetails(id: number) {
    this.getDetails.emit(id);
  }
}
