import {Component, OnInit} from '@angular/core';
import {InvitationApiService} from '../invitation-service/invitation-api.service';
import {DatePipe, NgForOf, NgIf, NgStyle} from '@angular/common';
import {MusicianApiService} from '../../musicians/musician-service/musician-api.service';
import {
  BandInvitation,
  BandInvitationRequest,
  BandJoinRequest,
  BandJoinUpdateRequest,
  RequestStatus
} from '../../core/models/invitation.model';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-invitations',
  standalone: true,
  imports: [
    NgIf,
    DatePipe,
    NgForOf,
    NgStyle
  ],
  templateUrl: './invitations.component.html',
  styleUrl: './invitations.component.css'
})
export class InvitationsComponent implements OnInit {
  activeTab: string = 'invitations';
  userId!: number;
  invitations: BandInvitation[] = [];
  hasUserBands: boolean = false;
  bandRequests: BandJoinRequest[] = [];

  constructor(private invitationApiService: InvitationApiService, private musicianApiService: MusicianApiService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    console.log("userId", this.userId);

    this.loadData();

    this.route.queryParams.subscribe(params => {
      this.activeTab = params['mode'];
    });
  }

  loadData() {
    this.musicianApiService.getMusicianBands(this.userId).subscribe(bands => {
      if(bands) {
        for (let band of bands) {
          if (band.founderId === this.userId) {
            this.hasUserBands = true;
            this.invitationApiService.getBandRequests(band.id).subscribe(requests => {
              if (requests) {
                this.bandRequests.push(...requests);
              }
            });
          }
        }
      }
    });

    this.invitationApiService.getUserInvitations().subscribe(invitations => {
      if (invitations) {
        this.invitations = invitations;
        console.log(this.invitations);
        console.log(invitations);
      }
    });
  }

  getStatusLabel(status: RequestStatus): string {
    switch (status) {
      case RequestStatus.ACCEPTED: return 'Zaakceptowane';
      case RequestStatus.REJECTED: return 'Odrzucone';
      case RequestStatus.PENDING: return 'Oczekujące';
      case RequestStatus.CANCELED: return 'Anulowane';
    }
  }


  respondRequest(request: BandJoinRequest, decision: boolean) {
    let req: BandJoinUpdateRequest = {
      id: request.id,
      bandId: request.bandId,
      applicantId: request.applicantId,
      status: decision ? RequestStatus.ACCEPTED : RequestStatus.REJECTED
    };

    this.invitationApiService.respondRequest(req).subscribe({
      next: () => {
        this.bandRequests = [];
        this.loadData();
      },
      error: err => {
        console.error('Błąd przy akceptacji/odrzuceniu zgłoszenia:', err);
      }
    });
  }

  respondInvitation(invitation: BandInvitationRequest, decision: boolean) {
    let inv: BandInvitationRequest = {
      id: invitation.id,
      bandId: invitation.bandId,
      invitedMusicianId: invitation.invitedMusicianId,
      status: decision ? RequestStatus.ACCEPTED : RequestStatus.REJECTED
    };

    this.invitationApiService.respondInvitation(inv).subscribe({
      next: () => {
        this.invitations = [];
        this.loadData();
      },
      error: err => {
        console.error('Błąd przy akceptacji/odrzuceniu zaproszenia:', err);
      }
    });
  }

  goToBand(id: number) {
    this.router.navigate(['/bands', id]);
  }
}
