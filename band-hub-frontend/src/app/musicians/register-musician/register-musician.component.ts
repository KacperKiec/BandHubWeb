import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {MusicAttributesApiService} from '../../services/music-attributes-service/music-attributes-api.service';
import {Instrument} from '../../core/models/instrument.model';
import {Genre} from '../../core/models/genre.model';
import {MusicianRequest} from '../../core/models/musician.model';

@Component({
  selector: 'app-register-musician',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf
  ],
  templateUrl: './register-musician.component.html',
  styleUrl: './register-musician.component.css'
})
export class RegisterMusicianComponent implements OnInit {
  availableGenres: Genre[] = [];
  availableInstruments: Instrument[] = [];

  showGenreDropdown = false;
  showInstrumentDropdown = false;
  @Input() userId!: number;
  @Output() registered: EventEmitter<MusicianRequest> = new EventEmitter();

  musician!: MusicianRequest;

  constructor(private musicAttributesApiService: MusicAttributesApiService) {
  }

  ngOnInit() {
    this.musicAttributesApiService.getInstruments().subscribe(instruments => {
      if(instruments) this.availableInstruments = instruments;
    });

    this.musicAttributesApiService.getGenres().subscribe(genres => {
      if(genres) this.availableGenres = genres;
    });

    this.musician = {
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
  }


  onInstrumentChange(event: Event, instrument: Instrument) {
    const input = event.target as HTMLInputElement;
    const checked = input.checked;

    if (checked) {
      this.musician.instruments.push(instrument);
    } else {
      this.musician.instruments = this.musician.instruments.filter(i => i.id !== instrument.id);
    }
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

  submit() {

    console.log('Muzyk zarejestrowany', this.musician);
    this.registered.emit(this.musician);
  }
}
