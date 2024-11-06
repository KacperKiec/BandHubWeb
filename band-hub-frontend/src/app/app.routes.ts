import {Routes} from '@angular/router';
import {BandsComponent} from './bands/bands/bands.component';
import {MusiciansComponent} from './musicians/musicians/musicians.component';
import {HomeComponent} from './start/home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'bands', component: BandsComponent },
  { path: 'musicians', component: MusiciansComponent }
]
