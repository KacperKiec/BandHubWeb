import {Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, Renderer2} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {Address} from '../../core/models/location.model';

@Component({
  selector: 'app-address-search',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf,
    NgClass
  ],
  templateUrl: './address-search.component.html',
  styleUrl: './address-search.component.css'
})
export class AddressSearchComponent implements OnInit, OnDestroy {
  searchTerm: string = '';
  suggestions: any[] = [];
  selectedAddress: Address = {
    street: '',
    number: '',
    city: '',
    postalCode: '',
    country: '',
    latitude: 0,
    longitude: 0
  };

  @Input() customClass = '';
  @Output() address: EventEmitter<Address> = new EventEmitter();
  private globalClickListener?: () => void;

  constructor(
    private http: HttpClient,
    private elRef: ElementRef,
    private renderer: Renderer2
  ) {}

  ngOnInit() {
    this.globalClickListener = this.renderer.listen('document', 'click', (event: MouseEvent) => {
      if (!this.elRef.nativeElement.contains(event.target)) {
        this.suggestions = [];
      }
    });
  }

  ngOnDestroy() {
    if (this.globalClickListener) {
      this.globalClickListener();
    }
  }

  onSearch() {
    if (this.searchTerm.length < 3) return;

    const apiKey = '51f135d1ff8946c19460f696588a73e6';
    const url = `https://api.geoapify.com/v1/geocode/autocomplete?text=${encodeURIComponent(this.searchTerm)}&limit=10&lang=pl&apiKey=${apiKey}&layers=locality,street&filter=countrycode:pl`;

    this.http.get<any>(url).subscribe(res => {
      this.suggestions = res.features
        .filter((feature: any) => {
          const props = feature.properties;

          const isUnwantedAdministrativeUnit =
            (props.county || props.municipality || props.state) &&
            !props.city && !props.town && !props.village;

          const isUnwantedByRegex =
            /^(powiat|gmina|województwo)\s/i.test(props.formatted);

          return !isUnwantedAdministrativeUnit && !isUnwantedByRegex;
        })
        .map((feature: any) => ({
          formatted: feature.properties.formatted,
          components: feature.properties
        }));
    });
  }

  selectSuggestion(suggestion: any) {
    const comp = suggestion.components;

    this.selectedAddress = {
      street: comp.street || '',
      number: comp.housenumber || '',
      city: comp.city || comp.town || comp.village || '',
      postalCode: comp.postcode || '',
      country: comp.country || '',
      latitude: comp.lat,
      longitude: comp.lon
    };

    this.searchTerm = suggestion.formatted;
    this.suggestions = [];
    this.address.emit(this.selectedAddress);
  }
}
