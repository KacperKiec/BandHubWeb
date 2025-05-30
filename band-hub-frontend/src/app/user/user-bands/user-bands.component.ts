import {Component, OnInit} from '@angular/core';
import {MusicianApiService} from '../../services/musician-service/musician-api.service';
import {BandApiService} from '../../services/band-service/band-api.service';
import {Router} from '@angular/router';
import {Band, BandRequest, BandUpdateRequest} from '../../core/models/band.model';
import {NgForOf, NgIf} from '@angular/common';
import {RegisterMusicianComponent} from '../../musicians/register-musician/register-musician.component';
import {RegisterBandComponent} from '../../bands/register-band/register-band.component';
import {EditBandComponent} from '../edit/edit-band/edit-band.component';

@Component({
  selector: 'app-user-bands',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    RegisterMusicianComponent,
    RegisterBandComponent,
    EditBandComponent
  ],
  templateUrl: './user-bands.component.html',
  styleUrl: './user-bands.component.css'
})
export class UserBandsComponent implements OnInit {

  userId!: number;
  userBands!: Band[];
  hasBands: boolean = false;
  showForm: boolean = false;
  editingMode: boolean = false;

  bandToEdit!: Band;

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
      }
    })
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  goToBands() {
    this.router.navigate(['/bands'])
  }

  createAnotherBand() {
    this.showForm = true;
    this.hasBands = false;
  }

  viewBandDetails(bandId: number) {
    this.router.navigate(['/bands', bandId]);
  }

  turnEditingMode(bandToEdit: Band) {
    this.editingMode = true;
    this.bandToEdit = bandToEdit;
  }

  leaveBand(bandId: number) {
    this.bandApiService.leaveBand({ bandId: bandId, musicianId: this.userId }).subscribe({
      next: () => {
        this.initData();
      },
      error: (err) => {
        console.error('Błąd przy opuszczaniu zespołu:', err);
      }
    });
  }

  removeBand(bandId: number) {
    const confirmed = window.confirm('Czy na pewno chcesz rozwiązać ten zespół? Tej operacji nie można cofnąć.');

    if (confirmed) {
      this.bandApiService.deleteBand(bandId).subscribe({
        next: () => {
          this.initData();
        },
        error: (err) => {
          console.error('Błąd podczas usuwania zespołu:', err);
        }
      });
    }
  }

  handleBandUpdating(band: BandUpdateRequest) {
    this.bandApiService.updateBand(band).subscribe(band => {
      if (band) {
        this.editingMode = false;
        this.initData();
      }
    })
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
