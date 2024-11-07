import { Component } from '@angular/core';
import {SearchBarComponent} from '../../shared/search-bar/search-bar.component';

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

}
