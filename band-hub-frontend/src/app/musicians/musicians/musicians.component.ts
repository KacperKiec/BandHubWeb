import {Component, Output} from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';
import {filterElement} from '../../core/models/types';

@Component({
  selector: 'app-musicians',
  standalone: true,
  imports: [
    SearchBarComponent
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
  @Output() placeholderText = 'Dane osobowe';
  searchingParams: {name: string, value: string}[]= [];

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
        finalData.push({ name: mappedName, value: [param.value] });
      }
    });

    return finalData;
  }
}
