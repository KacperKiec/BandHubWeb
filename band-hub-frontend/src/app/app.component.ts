import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from './global-components/header/header.component';
import {FooterComponent} from './global-components/footer/footer.component';
import {NavbarComponent} from './global-components/navbar/navbar.component';
import {LoaderComponent} from './core/loading/loader/loader.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, NavbarComponent, LoaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'band-hub-frontend';

  isMenuOpen = false;

  setMenuStatus(open: boolean) {
    this.isMenuOpen = open;
  }
}
