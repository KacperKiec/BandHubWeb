import {Component, OnInit} from '@angular/core';
import {EditAccountFormComponent} from '../edit-account-form/edit-account-form.component';
import {NgIf} from '@angular/common';
import {EditMusicianFormComponent} from '../../../musicians/edit-musician-form/edit-musician-form.component';
import {EditPasswordFormComponent} from '../edit-password-form/edit-password-form.component';
import {UserApiService} from '../../user-service/user-api.service';
import {MusicianApiService} from '../../../musicians/musician-service/musician-api.service';
import {AccountResponse} from '../../../core/models/account.model';
import {Musician} from '../../../core/models/musician.model';

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
