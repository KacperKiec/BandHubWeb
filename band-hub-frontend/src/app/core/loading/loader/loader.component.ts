import { Component } from '@angular/core';
import {LoaderService} from '../service/loader.service';
import {AsyncPipe, NgIf} from '@angular/common';

@Component({
  selector: 'app-loader',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe
  ],
  templateUrl: './loader.component.html',
  styleUrl: './loader.component.css'
})
export class LoaderComponent {
  constructor(public loadingService: LoaderService) {}
}
