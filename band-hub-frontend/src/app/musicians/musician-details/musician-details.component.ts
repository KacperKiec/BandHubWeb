import { Component } from '@angular/core';
import {MusicianApiService} from '../../services/musician-service/musician-api.service';
import {ActivatedRoute} from '@angular/router';
import {Musician} from '../../core/models/types';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';
import {NgForOf, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-musician-details',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage
  ],
  templateUrl: './musician-details.component.html',
  styleUrl: './musician-details.component.css'
})
export class MusicianDetailsComponent {

  musician!: Musician;

  sampleMusician: Musician = <Musician>{
    id: 1,
    firstName: 'Davie',
    lastName: 'Jones',
    birthday: new Date(),
    gender: 'male',
    city: 'Krakow',
    bio: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec pretium, nisi non vestibulum faucibus, massa nisl molestie risus, id hendrerit eros urna molestie libero. Ut malesuada vestibulum ante id porttitor. Nunc gravida vitae risus nec cursus. Nam eros orci, commodo at odio in, rhoncus facilisis dui. Suspendisse malesuada sem vel vehicula mattis. Nulla vel urna et arcu vulputate elementum quis in felis. Etiam quis mi quis nibh elementum blandit. Nunc eu molestie justo, feugiat sodales metus. Fusce non arcu vulputate, molestie velit eu, feugiat mauris. Cras molestie lectus a diam interdum accumsan. Proin nibh risus, fringilla a lorem in, placerat volutpat massa. Donec nec rhoncus magna, a varius sapien.',
    isLookingBand: true,
    genres: [Genre.METAL, Genre.ROCK],
    instruments: [Instrument.BASS, Instrument.KEYBOARD],
    image: '/assets/user.jpg'
  };

  constructor(private route: ActivatedRoute, private musicianApiService: MusicianApiService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    // this.musicianApiService.getMusicianById(id).subscribe(musician => {
    //   this.musician = musician;
    // });

    this.musician = this.sampleMusician;
  }

  calculateAge(birthday: Date): number {
    const birth = new Date(birthday);
    const ageDifMs = Date.now() - birth.getTime();
    return Math.floor(ageDifMs / (1000 * 60 * 60 * 24 * 365.25));
  }

  startChat(id: number) {

  }

}
