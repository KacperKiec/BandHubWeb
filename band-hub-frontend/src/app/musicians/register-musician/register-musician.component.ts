import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MusicAttributesApiService} from '../../shared/music-attributes-service/music-attributes-api.service';
import {Instrument} from '../../core/models/instrument.model';
import {Genre} from '../../core/models/genre.model';
import {MusicianRequest} from '../../core/models/musician.model';
import {MusicianInputComponent} from '../musician-input/musician-input.component';

@Component({
  selector: 'app-register-musician',
  standalone: true,
  imports: [
    FormsModule,
    MusicianInputComponent
  ],
  templateUrl: './register-musician.component.html',
  styleUrl: './register-musician.component.css'
})
export class RegisterMusicianComponent implements OnInit {
  availableGenres: Genre[] = [];
  availableInstruments: Instrument[] = [];
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

  getMusician(value: MusicianRequest) {
    this.musician = {
      id: this.userId,
      bio: value.bio,
      isLookingBand: value.isLookingBand,
      genres: value.genres,
      instruments: value.instruments,
    };
  }

  submit() {
    console.log('Muzyk zarejestrowany', this.musician);
    this.registered.emit(this.musician);
  }
}
