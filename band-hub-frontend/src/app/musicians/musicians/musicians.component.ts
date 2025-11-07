import {Component, OnInit} from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';
import {filterElement, SearchDisplayItem} from '../../core/models/shared.model';
import {SearchResultComponent} from '../../shared/search-result/search-result.component';
import {MusicianApiService} from '../musician-service/musician-api.service';
import {Router} from '@angular/router';
import {Musician} from '../../core/models/musician.model';
import {MusicAttributesApiService} from '../../shared/music-attributes-service/music-attributes-api.service';
import {forkJoin} from 'rxjs';

@Component({
  selector: 'app-musicians',
  standalone: true,
  imports: [
    SearchBarComponent,
    SearchResultComponent
  ],
  templateUrl: './musicians.component.html',
  styleUrl: './musicians.component.css'
})
export class MusiciansComponent implements OnInit {
  instruments: string[] = [];
  genres: string[] = [];

  filters: {label: string, type: string, options?: string[]}[] = [];
  placeholderText = 'Imie i Nazwisko';
  musiciansToDisplay: SearchDisplayItem[] = [];

  searchingParams: {name: string, value: string}[]= [];
  preparedParams: {name: string, value: string[]}[]=[];

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  constructor(private musicianApiService: MusicianApiService, private router: Router, private musicAttributesApiService: MusicAttributesApiService) {
  }

  ngOnInit() {
    forkJoin({
      genres: this.musicAttributesApiService.getGenres(),
      instruments: this.musicAttributesApiService.getInstruments()
    }).subscribe(({ genres, instruments }) => {
      this.genres = genres?.map(g => g.name) ?? [];
      this.instruments = instruments?.map(i => i.name) ?? [];
      this.buildFilters();
      this.loadPage(this.currentPage);
    });
  }

  loadPage(page: number) {
    this.musicianApiService.getMusiciansWithParams(this.preparedParams, page, this.pageSize).subscribe({
      next: (response) => {
        this.totalPages = response.page.totalPages;
        this.currentPage = response.page.number;
        this.musiciansToDisplay = response.content.map(this.mapMusicianToDisplayItem);
      },
      error: (err) => console.error('Error occurred:', err)
    });
  }

  goToDetails(id: number) {
    this.router.navigate(['/musicians', id]);
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
        "Instrument": "instruments",
        "Szuka zespołu": "isLookingForBand",
        "Wiek": "age",
        "Imie i Nazwisko": "name"
      };

      const mappedName = nameMapping[param.name];

      const temp = finalData.find(entry => entry.name === mappedName);
      if (temp) {
        temp.value.push(param.value);
      } else {
        if (mappedName === 'name') {
          let temp = param.value.split(' ');
          if (temp.length > 1) {
            finalData.push({name: 'firstName', value: [temp[0]]});
            finalData.push({name: 'lastName', value: [temp[1]]});
          } else {
            finalData.push({name: 'firstName', value: [temp[0]]});
            finalData.push({name: 'lastName', value: [temp[0]]});
          }
        }
        else finalData.push({ name: mappedName, value: [param.value] });
      }
    });

    return finalData;
  }

  private mapMusicianToDisplayItem(musician: Musician): SearchDisplayItem {
    return {
      id: musician.id,
      name: `${musician.firstName} ${musician.lastName}`,
      city: musician.city,
      genres: musician.genres,
      isLookingFor: musician.isLookingBand,
      image: musician.imageURL ?? '/assets/user.jpg'
    };
  }

  private buildFilters() {
    this.filters = [
      {label: "Miasto", type: "text"},
      {label: "Gatunek", type: "select", options: this.genres},
      {label: "Instrument", type: "select", options: this.instruments},
      {label: "Szuka zespołu", type: "checkbox"},
      {label: "Wiek", type: "range", options: ['1', '99']}
    ];
  }
}
