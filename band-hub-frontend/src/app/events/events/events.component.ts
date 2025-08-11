import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {EventDTO, EventSearchingParams} from '../../core/models/event.model';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {EventApiService} from '../event-service/event-api.service';
import {AddressSearchComponent} from '../../shared/address-search/address-search.component';
import {Address} from '../../core/models/location.model';
import {debounceTime, Subject} from 'rxjs';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [
    DatePipe,
    NgForOf,
    FormsModule,
    NgIf,
    AddressSearchComponent
  ],
  templateUrl: './events.component.html',
  styleUrl: './events.component.css'
})
export class EventsComponent implements OnInit {
  events: EventDTO[] = [];

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

  constructor(private eventApiService: EventApiService, private cdr: ChangeDetectorRef) {
    this.bandNameChanged.pipe(
      debounceTime(500)
    ).subscribe(() => {
      this.filterEvents();
    });
  }

  ngOnInit(): void {
    // this.events = this.getMockEvents(20);
    this.loadPage(this.currentPage);
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
    console.log('view', event.id);
    // router.navigate(['/events', event.id]) itd.
  }

  toggleFollow(event: EventDTO): void {
    console.log('toggle follow', event.id);
    // wywołaj endpoint follow/unfollow i zaktualizuj followersCount
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
