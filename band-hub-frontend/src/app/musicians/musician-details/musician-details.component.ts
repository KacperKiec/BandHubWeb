import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {MusicianApiService} from '../musician-service/musician-api.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {Musician} from '../../core/models/musician.model';
import {Band} from '../../core/models/band.model';
import {InvitationApiService} from '../../invitations/invitation-service/invitation-api.service';

@Component({
  selector: 'app-musician-details',
  standalone: true,
  imports: [
    NgForOf,
    NgOptimizedImage,
    NgIf
  ],
  templateUrl: './musician-details.component.html',
  styleUrl: './musician-details.component.css'
})
export class MusicianDetailsComponent implements OnInit{

  userId!:number;
  bandsToInvite: Band[] = new Array<Band>();
  showInvitationDialog: boolean = false;

  musician!: Musician;
  musicianBands!: Band[];

  @ViewChild('invitationPanel') panelRef!: ElementRef;

  constructor(private router: Router, private route: ActivatedRoute, private musicianApiService: MusicianApiService, private eRef: ElementRef, private invitationApiService: InvitationApiService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.musicianApiService.getMusicianById(id).subscribe(musician => {
      if(!musician.image) {
        musician.image = 'assets/user.jpg'
      }
      this.musician = musician;
    });

    this.musicianApiService.getMusicianBands(id).subscribe(bands => {
      this.musicianBands = bands;
    })

    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.musicianApiService.getMusicianBands(this.userId).subscribe(bands => {
      if(bands) {
        bands.forEach(band => {
          if(band.founderId === this.userId && !this.musicianBands.find(musicianBand => musicianBand.id === band.id)) {
            this.bandsToInvite.push(band);
          }
        })
      }
    });
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: MouseEvent) {
    const clickedInsideDropdown = this.panelRef?.nativeElement.contains(event.target);
    const clickedToggle = this.eRef.nativeElement.querySelector('.action-with-dialog')?.contains(event.target);

    if (!clickedInsideDropdown && !clickedToggle) {
      this.showInvitationDialog = false;
    }
  }

  calculateAge(birthday: Date): number {
    const birth = new Date(birthday);
    const ageDifMs = Date.now() - birth.getTime();
    return Math.floor(ageDifMs / (1000 * 60 * 60 * 24 * 365.25));
  }

  getBandDetails(id: number) {
    this.router.navigate(['/bands', id]);
  }

  startChat(musician: Musician) {
    this.router.navigate(['/chat'], { queryParams: { receiverFirstName: musician.firstName, receiverLastName: musician.lastName  }});
  }

  openInvitationPanel() {
    this.showInvitationDialog = true;
  }

  sendInvitation(bandId: number) {
    this.invitationApiService.sendInvitation({bandId: bandId, musicianId: this.musician.id}).subscribe(() => {
      this.showInvitationDialog = false;
      alert("Wysłano zaproszenie.");
    });
  }
}
