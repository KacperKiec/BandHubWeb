import {Component, OnInit} from '@angular/core';
import {UserApiService} from '../user-service/user-api.service';
import {NgForOf, NgIf} from '@angular/common';
import {MusicianApiService} from '../../musicians/musician-service/musician-api.service';
import {Router} from '@angular/router';
import {RegisterMusicianComponent} from '../../musicians/register-musician/register-musician.component';
import {AccountResponse} from '../../core/models/account.model';
import {Musician, MusicianRequest} from '../../core/models/musician.model';
import {Band} from '../../core/models/band.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    RegisterMusicianComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  profile!: AccountResponse;
  musician!: Musician;
  bands!: Band[];
  isMusician: boolean = false;
  showForm: boolean = false;

  constructor(private userApiService: UserApiService, private musicianApiService: MusicianApiService, private router: Router) {
  }

  ngOnInit() {
    this.initData();
  }

  calculateAge(birthday: Date): number {
    const birth = new Date(birthday);
    const ageDifMs = Date.now() - birth.getTime();
    return Math.floor(ageDifMs / (1000 * 60 * 60 * 24 * 365.25));
  }

  showRegistrationForm() {
    this.showForm = true;
  }

  editData() {
    this.router.navigate(['/profile/edit'])
  }

  initData() {
    this.userApiService.getCurrentUser().subscribe({
      next: user => {
        this.profile = user;
        this.musicianApiService.getMusician().subscribe(musician => {
          if (musician) {
            this.isMusician = true;
            this.musician = musician;
            this.musicianApiService.getMusicianBands(this.profile.id).subscribe(bands => {
              if (bands) {
                this.bands = bands;
              }
            });
          } else this.isMusician = false;
        });
      },
      error: err => {
        console.error('Nie można pobrać danych użytkownika', err);
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }

  handleMusicianRegistration(event: MusicianRequest) {
    this.musicianApiService.addMusician(event).subscribe(musician => {
      if (musician) {
        this.musician = musician;
        this.isMusician = true;
      }
    });
    this.initData();
  }

  getBandDetails(id: number) {
    this.router.navigate(['/bands', id]);
  }
}
