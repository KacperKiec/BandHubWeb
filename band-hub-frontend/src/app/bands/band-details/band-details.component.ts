import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BandApiService} from '../band-service/band-api.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {Band} from '../../core/models/band.model';
import {MusicianApiService} from '../../musicians/musician-service/musician-api.service';
import {InvitationApiService} from '../../invitations/invitation-service/invitation-api.service';
import {Instrument} from '../../core/models/instrument.model';
import {ImageApiService} from '../../shared/image-service/image-api.service';

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

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private bandApiService: BandApiService,
              private musicianApiService: MusicianApiService,
              private invitationApiService: InvitationApiService,
              private imageApiService: ImageApiService) {}

  ngOnInit() {
    const rawId = localStorage.getItem('userId');
    if (rawId) {
      this.userId = JSON.parse(rawId);
    }

    this.initData();
  }

  initData() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.bandApiService.getBandById(id).subscribe(band => {
      if (band) {
        this.band = band;
      }
    });

    if (this.userId) {
      this.musicianApiService.getMusicianById(this.userId).subscribe(musician => {
        this.isUserMusician = !!musician;
      });
    }
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

  onEditImageClick() {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.imageApiService.saveImage(file, this.band.imageURL || '').subscribe(imageURL => {
        if (imageURL) {
          this.bandApiService.updateImage(imageURL.url).subscribe({
            next: () => {
              this.initData();
            }
          });
        }
      });
    }
  }
}
