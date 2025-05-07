import {Component, ElementRef, EventEmitter, OnDestroy, OnInit, Output, Renderer2} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {Address} from '../../core/models/types';

@Component({
  selector: 'app-address-search',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    NgForOf
  ],
  templateUrl: './address-search.component.html',
  styleUrl: './address-search.component.css'
})
export class AddressSearchComponent implements OnInit, OnDestroy{
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

  @Output() address: EventEmitter<Address> = new EventEmitter();
  private globalClickListener?: () => void;

  constructor(private http: HttpClient, private elRef: ElementRef, private renderer: Renderer2) {}

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
    const url = `https://api.geoapify.com/v1/geocode/autocomplete?text=${encodeURIComponent(this.searchTerm)}&limit=5&lang=pl&apiKey=${apiKey}`;

    this.http.get<any>(url).subscribe(res => {
      this.suggestions = res.features.map((feature: any) => ({
        formatted: feature.properties.formatted,
        components: feature.properties
      }));
    });
  }

  selectSuggestion(suggestion: any) {
    const comp = suggestion.components;

    console.log('Debug komponentów Geoapify:', suggestion.components);

    this.selectedAddress = {
      street: comp.street || '',
      number: comp.housenumber || '',
      city: comp.city || comp.town || comp.village || comp.county || comp.state || '',
      postalCode: comp.postcode || '',
      country: comp.country || '',
      latitude: comp.lat,
      longitude: comp.lon
    };

    this.searchTerm = suggestion.formatted;
    this.suggestions = [];
    console.log('Wybrany adres:', this.selectedAddress);

    this.address.emit(this.selectedAddress);
  }

}
