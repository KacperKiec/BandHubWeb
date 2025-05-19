import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BandApiService} from '../../services/band-service/band-api.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {Band} from '../../core/models/band.model';
import {Musician} from '../../core/models/musician.model';

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
export class BandDetailsComponent implements OnInit{

  band!: Band;

  sampleBand: Band = <Band>{
    id: 1,
    name: 'AjronWejder',
    city: 'Krakow',
    country: 'Poland',
    bio: 'biobiobiobiob',
    isLookingMembers: true,
    genres: [],
    musicians: [
      <Musician>{
        id: 1,
        firstName: 'Davie',
        lastName: 'Jones',
        birthday: new Date(),
        gender: 'male',
        city: 'Krakow',
        country: 'Germany',
        bio: 'Davie Jones` bio',
        isLookingBand: true,
        genres: [],
        instruments: []
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        country: 'Germany',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [],
        instruments: []
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        country: 'Germany',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [],
        instruments: []
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        country: 'Germany',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [],
        instruments: []
      },
      <Musician>{
        id: 2,
        firstName: 'Arnold',
        lastName: 'Putler',
        birthday: new Date(),
        gender: 'female',
        city: 'Berlin',
        country: 'Germany',
        bio: 'Arnold Putler`s bio',
        isLookingBand: false,
        genres: [],
        instruments: []
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
