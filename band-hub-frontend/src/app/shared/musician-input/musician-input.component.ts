import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges, ViewChild
} from '@angular/core';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';
import {MusicianRequest} from '../../core/models/musician.model';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-musician-input',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './musician-input.component.html',
  styleUrl: './musician-input.component.css'
})
export class MusicianInputComponent implements OnInit, OnChanges {

  showGenreDropdown = false;
  showInstrumentDropdown = false;
  @Input() userId!: number;
  @Input() inputMusician!: MusicianRequest;
  @Input() availableGenres: Genre[] = [];
  @Input( )availableInstruments: Instrument[] = [];
  @Output() outputMusician: EventEmitter<MusicianRequest> = new EventEmitter();

  @ViewChild('genreSelect') genreSelectRef!: ElementRef;
  @ViewChild('instrumentSelect') instrumentSelectRef!: ElementRef;

  musician!: MusicianRequest;

  constructor() {
  }

  ngOnInit() {
    this.initMusician()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['inputMusician'] && changes['inputMusician'].currentValue) {
      this.initMusician();
    }
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    const clickedElement = event.target as HTMLElement;

    const clickedInsideGenre = this.genreSelectRef?.nativeElement.contains(clickedElement);
    const clickedInsideInstrument = this.instrumentSelectRef?.nativeElement.contains(clickedElement);

    if (!clickedInsideGenre) {
      this.showGenreDropdown = false;
    }

    if (!clickedInsideInstrument) {
      this.showInstrumentDropdown = false;
    }
  }

  private initMusician() {
    this.musician = this.inputMusician ?? {
      id: this.userId,
      bio: '',
      isLookingBand: false,
      genres: [],
      instruments: []
    };
  }

  toggleGenreDropdown() {
    this.showGenreDropdown = !this.showGenreDropdown;
  }

  toggleInstrumentDropdown() {
    this.showInstrumentDropdown = !this.showInstrumentDropdown;
  }

  onGenreChange(event: Event, genre: Genre) {
    const input = event.target as HTMLInputElement;
    const checked = input.checked;

    if (checked) {
      this.musician.genres.push(genre);
    } else {
      this.musician.genres = this.musician.genres.filter(g => g.id !== genre.id);
    }
    this.emit()
  }

  onInstrumentChange(event: Event, instrument: Instrument) {
    const input = event.target as HTMLInputElement;
    const checked = input.checked;

    if (checked) {
      this.musician.instruments.push(instrument);
    } else {
      this.musician.instruments = this.musician.instruments.filter(i => i.id !== instrument.id);
    }
    this.emit();
  }

  getSelectedGenresText(): string {
    return this.musician.genres.length
      ? this.musician.genres.map(g => g.name).join(', ')
      : 'Wybierz gatunki...';
  }

  getSelectedInstrumentText(): string {
    return this.musician.instruments.length
      ? this.musician.instruments.map(i => i.name).join(', ')
      : 'Wybierz gatunki...';
  }

  isGenreSelected(genre: Genre): boolean {
    return this.musician.genres.some(g => g.id === genre.id);
  }

  isInstrumentSelected(instrument: Instrument): boolean {
    return this.musician.instruments.some(i => i.id === instrument.id);
  }

  emit() {
    this.outputMusician.emit(this.musician);
  }
}
