import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input, OnChanges, OnInit,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {Genre} from '../../core/models/genre.model';
import {BandRequest} from '../../core/models/band.model';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {AddressSearchComponent} from '../address-search/address-search.component';
import {Address} from '../../core/models/location.model';

@Component({
  selector: 'app-band-input',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    AddressSearchComponent
  ],
  templateUrl: './band-input.component.html',
  styleUrl: './band-input.component.css'
})
export class BandInputComponent implements OnInit, OnChanges {
  showGenreDropdown = false;
  @Input() userId!: number;
  @Input() inputBand!: BandRequest;
  @Input() availableGenres: Genre[] = [];
  @Output() outputBand: EventEmitter<BandRequest> = new EventEmitter();

  @ViewChild('genreSelect') genreSelectRef!: ElementRef;

  band!:BandRequest;

  constructor() {
  }

  ngOnInit() {
    this.initBand()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputBand'] && changes['inputBand'].currentValue) {
      this.initBand();
    }
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const clickedElement = event.target as HTMLElement;

    const clickedInsideGenre = this.genreSelectRef?.nativeElement.contains(clickedElement);

    if (!clickedInsideGenre) {
      this.showGenreDropdown = false;
    }
  }

  private initBand() {
    this.band = this.inputBand ?? {
      name: '',
      city: '',
      country: '',
      bio: '',
      isLookingMembers: false,
      genres: [],
      founderId: this.userId
    };
  }

  toggleGenreDropdown() {
    this.showGenreDropdown = !this.showGenreDropdown;
  }

  onGenreChange(event: Event, genre: Genre) {
    const input = event.target as HTMLInputElement;
    const checked = input.checked;

    if (checked) {
      this.band.genres.push(genre);
    } else {
      this.band.genres = this.band.genres.filter(g => g.id !== genre.id);
    }
    this.emit()
  }

  getSelectedGenresText(): string {
    return this.band.genres.length
      ? this.band.genres.map(g => g.name).join(', ')
      : 'Wybierz gatunki...';
  }

  isGenreSelected(genre: Genre): boolean {
    return this.band.genres.some(g => g.id === genre.id);
  }

  getAddress(address: Address) {
    this.band.city = address.city;
    this.band.country = address.country;
    this.emit();
  }

  emit() {
    this.outputBand.emit(this.band);
  }
}
