import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Genre} from '../../core/models/genre.model';
import {MusicAttributesApiService} from '../../services/music-attributes-service/music-attributes-api.service';
import {BandRequest} from '../../core/models/band.model';
import {MusicianInputComponent} from '../../shared/musician-input/musician-input.component';
import {BandInputComponent} from '../../shared/band-input/band-input.component';

@Component({
  selector: 'app-register-band',
  standalone: true,
  imports: [
    MusicianInputComponent,
    BandInputComponent
  ],
  templateUrl: './register-band.component.html',
  styleUrl: './register-band.component.css'
})
export class RegisterBandComponent implements OnInit {
  availableGenres: Genre[] = [];
  @Input() userId!: number;
  @Output() registered: EventEmitter<BandRequest> = new EventEmitter();

  band!:BandRequest;

  constructor(private musicAttributesApiService: MusicAttributesApiService) {
  }

  ngOnInit() {
    this.musicAttributesApiService.getGenres().subscribe(genres => {
      if(genres) this.availableGenres = genres;
    });

    this.band = {
      name: '',
      city: '',
      country: '',
      bio: '',
      isLookingMembers: false,
      genres: [],
      founderId: this.userId
    };
  }

  getBand(value: BandRequest) {
    this.band = {
      name: value.name,
      city: value.city,
      country: value.country,
      bio: value.bio,
      isLookingMembers: value.isLookingMembers,
      genres: value.genres,
      founderId: this.userId
    };
  }

  submit() {
    console.log('Zespół zarejestrowany', this.band);
    this.registered.emit(this.band);
  }
}
