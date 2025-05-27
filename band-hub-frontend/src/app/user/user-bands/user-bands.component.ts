import {Component, OnInit} from '@angular/core';
import {MusicianApiService} from '../../services/musician-service/musician-api.service';
import {BandApiService} from '../../services/band-service/band-api.service';
import {Router} from '@angular/router';
import {Band, BandRequest} from '../../core/models/band.model';
import {NgForOf, NgIf} from '@angular/common';
import {RegisterMusicianComponent} from '../../musicians/register-musician/register-musician.component';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';

@Component({
  selector: 'app-user-bands',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    RegisterMusicianComponent,
    RegisterBandComponent
  ],
  templateUrl: './user-bands.component.html',
  styleUrl: './user-bands.component.css'
})
export class UserBandsComponent implements OnInit {

  userId!: number;
  userBands!: Band[];
  hasBands: boolean = false;
  showForm: boolean = false;

  constructor(private musicianApiService: MusicianApiService, private bandApiService: BandApiService, private router: Router) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.initData();
  }

  initData() {
    this.musicianApiService.getMusicianBands(this.userId).subscribe(bands => {
      if (bands) {
        this.userBands = bands;
        this.hasBands = this.userBands.length > 0;
        console.log(this.userBands, this.hasBands);
      }
    })
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  handleBandRegistration(band: BandRequest) {
    this.bandApiService.addBand(band).subscribe(band => {
      if (band) {
        this.userBands.push(band);
        this.hasBands = true;
      }
      this.initData();
    })
  }

}
