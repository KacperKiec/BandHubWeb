import {Component, OnInit} from '@angular/core';
import {EditAccountFormComponent} from '../edit-account-form/edit-account-form.component';
import {NgIf} from '@angular/common';
import {EditMusicianFormComponent} from '../edit-musician-form/edit-musician-form.component';
import {EditPasswordFormComponent} from '../edit-password-form/edit-password-form.component';
import {AccountResponse, Musician} from '../../../core/models/types';
import {UserApiService} from '../../../services/user-service/user-api.service';
import {MusicianApiService} from '../../../services/musician-service/musician-api.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [
    EditAccountFormComponent,
    NgIf,
    EditMusicianFormComponent,
    EditPasswordFormComponent
  ],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent implements OnInit {
  activeTab: string = 'account';
  profile!: AccountResponse;
  musician!: Musician;
  isMusician: boolean = false;

  constructor(private userApiService: UserApiService, private musicianApiService: MusicianApiService) {
  }

  ngOnInit() {
    this.userApiService.getCurrentUser().subscribe(acc => {
      if(acc) {
        this.profile = acc;
      }
      this.musicianApiService.getMusicianById(this.profile.id).subscribe(musician => {
        if(musician) {
          this.isMusician = true;
          this.musician = musician;
        }
        else this.isMusician = false;
      })
    });

  }
}
