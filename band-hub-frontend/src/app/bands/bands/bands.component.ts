import {Component, Output} from '@angular/core';
import {SearchBarComponent} from "../../shared/search-bar/search-bar.component";
import {SearchResultComponent} from "../../shared/search-result/search-result.component";
import {GenreEnum} from '../../core/models/genre.model';
import {Band, filterElement, SearchDisplayItem} from '../../core/models/shared.model';
import {Router} from '@angular/router';
import {BandApiService} from '../../services/band-service/band-api.service';

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
export class BandsComponent {
  @Output()filters: {label: string, type: string, options?: string[]}[] = [
    {label: "Miasto", type: "text"},
    {label: "Gatunek", type: "select", options: Object.values(GenreEnum)},
    {label: "Szuka muzyków", type: "checkbox"}
  ];
  @Output() placeholderText = 'Nazwa';
  @Output() bandsToDisplay: SearchDisplayItem[];

  searchingParams: {name: string, value: string}[]= [];
  preparedParams: {name: string, value: string[]}[]=[];

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

  constructor(private router: Router, private bandApiService: BandApiService) {
    this.bandsToDisplay = this.sampleBands.map(this.mapBandToDisplayItem);
  }

  ngOnInit() {
    this.loadPage(this.currentPage);
  }

  loadPage(page: number) {
    this.bandApiService.getBandsWithParams(this.preparedParams, page, this.pageSize).subscribe({
      next: (response) => {
        console.log(response);
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

  getParams(data: filterElement[]): void{
    this.searchingParams = data;
    this.preparedParams = this.prepareData();
    console.log(this.preparedParams);
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
}
