import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';
import {PersonalInfoInputComponent} from '../../../shared/personal-info-input/personal-info-input.component';
import {BandInputComponent} from '../../../shared/band-input/band-input.component';
import {Genre} from '../../../core/models/genre.model';
import {Band, BandRequest, BandUpdateRequest} from '../../../core/models/band.model';
import {MusicAttributesApiService} from '../../../services/music-attributes-service/music-attributes-api.service';

@Component({
  selector: 'app-edit-band',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    PersonalInfoInputComponent,
    BandInputComponent
  ],
  templateUrl: './edit-band.component.html',
  styleUrl: './edit-band.component.css'
})
export class EditBandComponent implements OnInit {
  availableGenres: Genre[] = [];
  @Input() userId!: number;
  @Input() bandTemplate!:Band;
  @Output() updated: EventEmitter<BandUpdateRequest> = new EventEmitter();


  band!:BandUpdateRequest;

  constructor(private musicAttributesApiService: MusicAttributesApiService) {
  }

  ngOnInit() {
    this.musicAttributesApiService.getGenres().subscribe(genres => {
      if(genres) this.availableGenres = genres;
    });
    if(this.bandTemplate) {
      this.band = {
        id: this.bandTemplate.id,
        name: this.bandTemplate.name,
        city: this.bandTemplate.city,
        country: this.bandTemplate.country,
        bio: this.bandTemplate.bio,
        isLookingMembers: this.bandTemplate.isLookingMembers,
        genres: this.bandTemplate.genres,
        founderId: this.userId
      };
    }
  }

  getBand(value: BandRequest) {
    this.band = {
      id: this.bandTemplate.id,
      name: value.name,
      city: value.city,
      country: value.country,
      bio: value.bio,
      isLookingMembers: value.isLookingMembers,
      genres: value.genres,
      founderId: this.userId
    };
  }

  submitForm() {
    this.updated.emit(this.band);
  }
}
