import { Component } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BandApiService} from '../../services/band-service/band-api.service';
import {Band, Musician} from '../../core/models/types';
import {GenreEnum} from '../../core/models/genre.model';
import {InstrumentEnum} from '../../core/models/instrument.model';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-band-details',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './band-details.component.html',
  styleUrl: './band-details.component.css'
})
export class BandDetailsComponent {

  band!: Band;

  sampleBand: Band = <Band>{
    id: 1,
    name: 'AjronWejder',
    city: 'Krakow',
    bio: 'biobiobiobiob',
    isLookingMembers: true,
    genres: [GenreEnum.METAL, GenreEnum.ROCK],
    musicians: [
      <Musician>{
        id: 1,
        firstName: 'Davie',
        lastName: 'Jones',
        birthday: new Date(),
        gender: 'male',
        city: 'Krakow',
        bio: 'Davie Jones` bio',
        isLookingBand: true,
        genres: [GenreEnum.METAL, GenreEnum.ROCK],
        instruments: [InstrumentEnum.BASS, InstrumentEnum.KEYBOARD]
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [GenreEnum.POP, GenreEnum.CLASSIC, GenreEnum.INDIE],
        instruments: [InstrumentEnum.VOICE, InstrumentEnum.DRUMS]
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [GenreEnum.POP, GenreEnum.CLASSIC, GenreEnum.INDIE],
        instruments: [InstrumentEnum.VOICE, InstrumentEnum.DRUMS]
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [GenreEnum.POP, GenreEnum.CLASSIC, GenreEnum.INDIE],
        instruments: [InstrumentEnum.VOICE, InstrumentEnum.DRUMS]
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [GenreEnum.POP, GenreEnum.CLASSIC, GenreEnum.INDIE],
        instruments: [InstrumentEnum.VOICE, InstrumentEnum.DRUMS]
      }
    ],
    founderId: 1,
    image: '/assets/user.jpg'
  }

  constructor(private router: Router, private route: ActivatedRoute, private bandApiService: BandApiService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.bandApiService.getBandById(id).subscribe(band => {
      if (!band.image) {
        band.image = 'assets/user.jpg';
      }
      this.band = band;
    });

    //this.band = this.sampleBand;
  }

  getMusicianDetails(id: number) {
    this.router.navigate(['/musicians', id]);
  }

  goToBandEvents() {
    console.log("band events");
  }
}
