import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BandApiService} from '../../services/band-service/band-api.service';
import {NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {Band} from '../../core/models/band.model';
import {Musician} from '../../core/models/musician.model';

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

  constructor(private router: Router, private route: ActivatedRoute, private bandApiService: BandApiService) {}

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
  }

  getMusicianDetails(id: number) {
    this.router.navigate(['/musicians', id]);
  }

  goToBandEvents() {
    console.log("band events");
  }

  isUserInBand(): boolean {
    return this.band?.musicians?.some(m => m.id === this.userId) ?? false;
  }

  sendRequest() {
    this.bandApiService.sendRequest({bandId: this.band.id, musicianId: this.userId}).subscribe({
      next: data => {
        alert("Wysłano zgłoszenie.");
      }
    });
  }
}
