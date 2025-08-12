import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BandApiService} from '../band-service/band-api.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {Band} from '../../core/models/band.model';
import {MusicianApiService} from '../../musicians/musician-service/musician-api.service';
import {InvitationApiService} from '../../invitations/invitation-service/invitation-api.service';
import {Instrument} from '../../core/models/instrument.model';

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
  userId!: number;
  isUserMusician: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private bandApiService: BandApiService, private musicianApiService: MusicianApiService, private invitationApiService: InvitationApiService) {}

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.bandApiService.getBandById(id).subscribe(band => {
      if (!band.image) {
        band.image = 'assets/user.jpg';
      }
      this.band = band;
    });

    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.musicianApiService.getMusicianById(this.userId).subscribe(musician => {
      this.isUserMusician = !!musician;
    });
  }

  getMusicianDetails(id: number) {
    this.router.navigate(['/musicians', id]);
  }

  goToBandEvents(bandName: string) {
    this.router.navigate(['/events'], {queryParams: { bandName }});
  }

  isUserInBand(): boolean {
    if(this.isUserMusician) {
      return this.band?.musicians?.some(m => m.id === this.userId) ?? false;
    } return true;
  }

  sendRequest() {
    this.invitationApiService.sendRequest({bandId: this.band.id, musicianId: this.userId}).subscribe({
      next: data => {
        alert("Wysłano zgłoszenie.");
      }
    });
  }

  getInstrumentLabel(instruments: Instrument[]): string {
    if (!instruments || instruments.length === 0) {
      return ' - Brak instrumentów';
    }

    const names = instruments.map(i => i.name);
    if (names.length <= 3) {
      return ' - ' + names.join(', ');
    }

    return ' - ' + names.slice(0, 3).join(', ') + ', ...';
  }

}
