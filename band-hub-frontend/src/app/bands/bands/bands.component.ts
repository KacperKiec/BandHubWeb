import {Component, OnInit} from '@angular/core';
import {SearchBarComponent} from "../../shared/search-bar/search-bar.component";
import {SearchResultComponent} from "../../shared/search-result/search-result.component";
import {filterElement, SearchDisplayItem} from '../../core/models/shared.model';
import {Router} from '@angular/router';
import {BandApiService} from '../band-service/band-api.service';
import {Band} from '../../core/models/band.model';
import {MusicAttributesApiService} from '../../shared/music-attributes-service/music-attributes-api.service';

@Component({
  selector: 'app-bands',
  standalone: true,
    imports: [
        SearchBarComponent,
        SearchResultComponent
    ],
  templateUrl: './bands.component.html',
  styleUrl: './bands.component.css'
})
export class BandsComponent implements OnInit {
  genres: string[] = [];

  placeholderText = 'Nazwa';
  bandsToDisplay: SearchDisplayItem[];
  searchingParams: {name: string, value: string}[]= [];
  preparedParams: {name: string, value: string[]}[]=[];
  filters: {label: string, type: string, options?: string[]}[] = [];

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  sampleBands: Band[] = [
    <Band>{
      id: 1,
      name: 'AjronWejder',
      city: 'Krakow',
      country: 'Poland',
      bio: 'biobiobiobiob',
      isLookingMembers: true,
      genres: [],
      musicians: [],
      founderId: 1
    }
  ];

  constructor(private router: Router, private bandApiService: BandApiService, private musicAttributesApiService: MusicAttributesApiService) {
    this.bandsToDisplay = this.sampleBands.map(this.mapBandToDisplayItem);
  }

  ngOnInit() {
    this.musicAttributesApiService.getGenres().subscribe(genres => {
      if (genres) {
        this.genres = genres.map(g => g.name);
        this.buildFilters();
      }
    });

    this.loadPage(this.currentPage);
  }


  loadPage(page: number) {
    this.bandApiService.getBandsWithParams(this.preparedParams, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.bandsToDisplay = response.content.map(this.mapBandToDisplayItem);
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }

  goToDetails(id: number) {
    this.router.navigate(['/bands', id]);
  }

  getParams(data: filterElement[]): void {
    this.searchingParams = data;
    this.preparedParams = this.prepareData();
    this.loadPage(0);
  }

  private prepareData(): {name: string, value: string[]}[] {
    const finalData: {name: string, value: string[]}[] = [];

    this.searchingParams.forEach(param => {

      const nameMapping: { [key: string]: string } = {
        "Miasto": "cities",
        "Gatunek": "genres",
        "Szuka muzyków": "isLookingMembers",
        "Nazwa": "name"
      };

      const mappedName = nameMapping[param.name];

      const temp = finalData.find(entry => entry.name === mappedName);
      if (temp) {
        temp.value.push(param.value);
      } else finalData.push({ name: mappedName, value: [param.value] });
    });

    return finalData;
  }

  private mapBandToDisplayItem(band: Band): SearchDisplayItem {
    return {
      id: band.id,
      name: band.name,
      city: band.city,
      genres: band.genres,
      isLookingFor: band.isLookingMembers,
      image: '/assets/user.jpg'
    };
  }

  private buildFilters() {
    this.filters = [
      { label: "Miasto", type: "text" },
      { label: "Gatunek", type: "select", options: this.genres },
      { label: "Szuka muzyków", type: "checkbox" }
    ];
  }
}
