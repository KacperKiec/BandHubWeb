import {Component, OnInit} from '@angular/core';
import {UserApiService} from '../../services/user-service/user-api.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {MusicianApiService} from '../../services/musician-service/musician-api.service';
import {Router} from '@angular/router';
import {RegisterMusicianComponent} from '../../musicians/register-musician/register-musician.component';
import {AccountResponse} from '../../core/models/account.model';
import {Musician, MusicianRequest} from '../../core/models/musician.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf,
    RegisterMusicianComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  profile!: AccountResponse;
  musician!: Musician;
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
    this.userApiService.getCurrentUser().subscribe(acc => {
      if(acc) {
        this.profile = acc;
        this.profile.image = 'assets/user.jpg'
      }
      this.musicianApiService.getMusicianById(this.profile.id).subscribe(musician => {
        if(musician) {
          this.isMusician = true;
          this.musician = musician;
          console.log(musician)
        }
        else this.isMusician = false;
      })
    });
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
}
