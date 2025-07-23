import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MusicianRequest} from '../../core/models/musician.model';
import {Router} from '@angular/router';
import {MusicianApiService} from '../musician-service/musician-api.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';
import {MusicAttributesApiService} from '../../shared/music-attributes-service/music-attributes-api.service';
import {MusicianInputComponent} from '../musician-input/musician-input.component';

@Component({
  selector: 'app-edit-musician-form',
  standalone: true,
  imports: [
    FormsModule,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    MusicianInputComponent
  ],
  templateUrl: './edit-musician-form.component.html',
  styleUrl: './edit-musician-form.component.css'
})
export class EditMusicianFormComponent implements OnInit {
  availableGenres: Genre[] = [];
  availableInstruments: Instrument[] = [];
  userId!: number;
  @Output() updated: EventEmitter<MusicianRequest> = new EventEmitter();

  musician!: MusicianRequest;

  constructor(private musicAttributesApiService: MusicAttributesApiService, private musicianApiService: MusicianApiService, private router: Router) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.musicAttributesApiService.getInstruments().subscribe(instruments => {
      if(instruments) this.availableInstruments = instruments;
    });

    this.musicAttributesApiService.getGenres().subscribe(genres => {
      if(genres) this.availableGenres = genres;
    });

    this.musicianApiService.getMusicianById(this.userId).subscribe(musician => {
      if(musician) this.musician = musician;
      else {
        this.musician = {
          id: this.userId,
          bio: '',
          isLookingBand: false,
          genres: [],
          instruments: []
        };
      }
    });
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

  updateMusician() {
    this.musicianApiService.updateMusician(this.musician).subscribe({
      next: () => {
        this.router.navigate(['/profile']);
      }
    });
  }
}
