import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {EventDTO, EventSearchingParams, FollowRequest, UserEventDTO} from '../../core/models/event.model';
import {DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {EventApiService} from '../event-service/event-api.service';
import {AddressSearchComponent} from '../../shared/address-search/address-search.component';
import {Address} from '../../core/models/location.model';
import {debounceTime, Subject} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {EventDetailsComponent} from '../event-details/event-details.component';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    FormsModule,
    NgIf,
    AddressSearchComponent,
    NgClass,
    EventDetailsComponent
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css',
  animations: [
    trigger('backdropFadeInOut', [
      state('void', style({ opacity: 0 })),
      state('*', style({ opacity: 1 })),
      transition('void <=> *', animate('200ms ease-in-out')),
    ]),
    trigger('detailsFadeInOut', [
      state('void', style({ opacity: 0, transform: 'translate(-50%, -50%) scale(0.9)' })),
      state('*', style({ opacity: 1, transform: 'translate(-50%, -50%) scale(1)' })),
      transition('void <=> *', animate('200ms ease-in-out')),
    ])
  ]
})
export class EventsComponent implements OnInit {
  events: EventDTO[] = [];
  userEvents: UserEventDTO[] = [];
  userId!: number;

  currentPage = 0;
  pageSize = 9;
  totalPages = 0;

  params: EventSearchingParams = {
    cities: [],
    date: null,
    bandName: ''
  };
  date: Date | null = null;
  bandName: string = '';
  bandNameChanged: Subject<string> = new Subject<string>();

  eventDetails!: EventDTO;
  isDetailsVisible = false;

  constructor(private eventApiService: EventApiService, private cdr: ChangeDetectorRef, private route: ActivatedRoute) {
    this.bandNameChanged.pipe(
      debounceTime(500)
    ).subscribe(() => {
      this.filterEvents();
    });
  }

  ngOnInit(): void {
    // this.events = this.getMockEvents(20);
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    if (this.userId) {
      this.eventApiService.getUserEvents(this.userId).subscribe(events => {
        if (events) {
          this.userEvents = events;
        }
      });
    }

    this.route.queryParams.subscribe(params => {
      if (params['bandName']) {
        this.bandName = params['bandName'];
        this.params.bandName = params['bandName'];
        this.filterEvents();
      } else {
        this.loadPage(this.currentPage);
      }
    });
  }

  onBandNameChange(newValue: string) {
    this.bandName = newValue;
    this.bandNameChanged.next(newValue);
  }

  getCity(value: Address) {
    this.params.cities = [];
    this.params.cities.push(value.city);
  }

  goToPage(page: number) {
    if (page >= 0 && page < this.totalPages) {
      this.loadPage(page)
    }
  }

  loadPage(page: number) {
    this.eventApiService.getEvents(this.params, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.events = [...response.content];
        this.cdr.detectChanges();
      }
    });
  }

  filterEvents() {
    this.params.date = this.date ? this.date.toString() : null;
    this.params.bandName = this.bandName;
    this.currentPage = 0;
    this.loadPage(this.currentPage);
  }

  resetParams() {
    this.params = {
      cities: [],
      date: null,
      bandName: ''
    };
    this.date = null;
    this.bandName = '';
    this.currentPage = 0;
    this.loadPage(this.currentPage);
  }

  viewDetails(event: EventDTO): void {
    this.isDetailsVisible = true;
    this.eventDetails = event;
  }

  closeDetails() {
    this.isDetailsVisible = false;
  }

  getTicketPrice(event: EventDTO) {
    if (event.ticketPrice === 0) {
      return 'Wstęp wolny';
    }
    return event.ticketPrice + ' PLN';
  }

  followEvent(event: EventDTO) {
    let fr: FollowRequest = {
      userId: this.userId,
      eventId: event.id
    };

    this.eventApiService.followEvent(fr).subscribe(userEvent => {
      if (userEvent) {
        this.userEvents.push(userEvent);
        event.followersCount++;
      }
    });
  }

  unfollowEvent(event: EventDTO) {
    let fr: FollowRequest = {
      userId: this.userId,
      eventId: event.id
    };

    this.eventApiService.unfollowEvent(fr).subscribe({
      next: () => {
        this.userEvents = this.userEvents.filter(e => e.eventId !== event.id);
        event.followersCount--;
      }
    });
  }

  isEventFollowed(eventId: number) {
    return this.userEvents.some(event => event.eventId === eventId);
  }

  private getMockEvents(n: number): EventDTO[] {
    const cities = ['Warszawa','Kraków','Wrocław','Gdańsk','Poznań'];
    const bands = ['The Echoes','Night Roads','Silver Strings','Velvet Riot','Moonbeat'];
    const venues = ['Klub A','Klub B','Sala Koncertowa','Scena Pod Chmurką','Cafe Jazz'];
    const res: EventDTO[] = [];
    for (let i = 1; i <= n; i++) {
      const city = cities[i % cities.length];
      const band = bands[i % bands.length];
      const venue = venues[i % venues.length];
      const date = new Date();
      date.setDate(date.getDate() + (i % 30));
      const isoDate = date.toISOString().slice(0,10);
      res.push({
        id: i,
        name: `${band} Live ${i}`,
        bandId: i,
        bandName: band,
        venueId: i,
        venueName: venue,
        bookingId: i,
        street: 'ul. Muzyczna',
        number: String(1 + (i % 30)),
        city: city,
        postalCode: '00-000',
        country: 'PL',
        date: isoDate,
        startTime: '19:00',
        endTime: '22:00',
        description: 'Wieczór pełen muzyki, świetna atmosfera i wyjątkowe aranżacje.',
        ticketPrice: 20 + (i % 5) * 10,
        followersCount: Math.floor(Math.random() * 300),
        creatorId: 1
      });
    }
    return res;
  }
}
