import {Component, Output} from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';
import {filterElement, Musician, SearchDisplayItem} from '../../core/models/types';
import {SearchResultComponent} from '../../shared/search-result/search-result.component';

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
export class MusiciansComponent {
  @Output()filters: {label: string, type: string, options?: string[]}[] = [
    {label: "Miasto", type: "text"},
    {label: "Gatunek", type: "select", options: Object.values(Genre)},
    {label: "Instrument", type: "select", options: Object.values(Instrument)},
    {label: "Szuka zespołu", type: "checkbox"},
    {label: "Wiek", type: "range", options: ['1', '99']}
  ];
  @Output() placeholderText = 'Imie i Nazwisko';
  @Output() musiciansToDisplay: SearchDisplayItem[];

  searchingParams: {name: string, value: string}[]= [];

  sampleMusicians: Musician[] = [
    <Musician>{
      id: 1,
      firstName: 'Davie',
      lastName: 'Jones',
      birthday: new Date(),
      gender: 'male',
      city: 'Krakow',
      bio: 'Davie Jones` bio',
      isLookingBand: true,
      genres: [Genre.METAL, Genre.ROCK],
      instruments: [Instrument.BASS, Instrument.KEYBOARD]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    },
    // <Musician>{
    //   id: 2,
    //   firstName: 'Arnold',
    //   lastName: 'Putler',
    //   birthday: new Date(),
    //   gender: 'female',
    //   city: 'Berlin',
    //   bio: 'Arnold Putler`s bio',
    //   isLookingBand: false,
    //   genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
    //   instruments: [Instrument.VOICE, Instrument.DRUMS]
    // },
    <Musician>{
      id: 2,
      firstName: 'Arnold',
      lastName: 'Putler',
      birthday: new Date(),
      gender: 'female',
      city: 'Berlin',
      bio: 'Arnold Putler`s bio',
      isLookingBand: false,
      genres: [Genre.POP, Genre.CLASSIC, Genre.INDIE],
      instruments: [Instrument.VOICE, Instrument.DRUMS]
    }
  ];

  constructor() {
    this.musiciansToDisplay = this.sampleMusicians.map(this.mapMusicianToDisplayItem);
  }

  getParams(data: filterElement[]): void{
    this.searchingParams = data;
    const preparedData = this.prepareData();
    console.log(preparedData);
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
