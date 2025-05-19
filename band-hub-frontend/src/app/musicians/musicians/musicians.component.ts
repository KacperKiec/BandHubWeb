import {Component, OnInit, Output} from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';
import {GenreEnum} from '../../core/models/genre.model';
import {InstrumentEnum} from '../../core/models/instrument.model';
import {filterElement, SearchDisplayItem} from '../../core/models/shared.model';
import {SearchResultComponent} from '../../shared/search-result/search-result.component';
import {MusicianApiService} from '../../services/musician-service/musician-api.service';
import {Router} from '@angular/router';
import {Musician} from '../../core/models/musician.model';

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
  @Output()filters: {label: string, type: string, options?: string[]}[] = [
    {label: "Miasto", type: "text"},
    {label: "Gatunek", type: "select", options: Object.values(GenreEnum)},
    {label: "Instrument", type: "select", options: Object.values(InstrumentEnum)},
    {label: "Szuka zespołu", type: "checkbox"},
    {label: "Wiek", type: "range", options: ['1', '99']}
  ];
  @Output() placeholderText = 'Imie i Nazwisko';
  @Output() musiciansToDisplay: SearchDisplayItem[];

  searchingParams: {name: string, value: string}[]= [];
  preparedParams: {name: string, value: string[]}[]=[];

  currentPage = 0;
  pageSize = 10;
  totalPages = 0;

  sampleMusicians: Musician[] = [
    <Musician>{
      id: 1,
      firstName: 'Davie',
      lastName: 'Jones',
      birthday: new Date(),
      gender: 'male',
      city: 'Krakow',
      country: 'Poland',
      bio: 'Davie Jones` bio',
      isLookingBand: true,
      genres: [],
      instruments: []
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      country: 'Germany',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [],
      instruments: []
    }
  ];

  constructor(private musicianApiService: MusicianApiService, private router: Router) {
    this.musiciansToDisplay = this.sampleMusicians.map(this.mapMusicianToDisplayItem);
  }

  ngOnInit() {
    this.loadPage(this.currentPage);
  }

  loadPage(page: number) {
    this.musicianApiService.getMusiciansWithParams(this.preparedParams, page, this.pageSize).subscribe({
      next: (response) => {
        console.log(response);
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
        "Instrument": "instruments",
        "Szuka zespołu": "isLookingForBand",
        "Wiek": "age",
        "Dane osobowe": "name"
      };

      const mappedName = nameMapping[param.name];

      const temp = finalData.find(entry => entry.name === mappedName);
      if (temp) {
        temp.value.push(param.value);
      } else {
        if (mappedName === 'name') {
          if (param.value.length > 1) {
            finalData.push({name: 'firstName', value: [param.value[0]]});
            finalData.push({name: 'lastName', value: [param.value[1]]});
          } else {
            finalData.push({name: 'lastName', value: [param.value[0]]});
            finalData.push({name: 'firstName', value: [param.value[0]]});
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
      image: '/assets/user.jpg'
    };
  }
}
