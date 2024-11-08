import {Component, Output} from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';
import {Genre} from '../../core/models/genre.model';
import {Instrument} from '../../core/models/instrument.model';

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
    {label: "Szuka zespołu", type: "checkbox"}
  ];
}
