import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from './global-components/header/header.component';
import {FooterComponent} from './global-components/footer/footer.component';
import {NavbarComponent} from './global-components/navbar/navbar.component';
import {LoaderComponent} from './core/loading/loader/loader.component';
import {AuthService} from './core/auth/service/auth.service';
import {ErrorPanelComponent} from './global-components/error-panel/error-panel.component';
import {ErrorHandlerService} from './core/error-handler-service/error-handler.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, NavbarComponent, LoaderComponent, ErrorPanelComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'band-hub-frontend';

  isMenuOpen = false;
  errorMessage: string = '';

  constructor(private authService: AuthService, private errorHandler: ErrorHandlerService) {
  }

  ngOnInit() {
    this.authService.checkLoginStatus();
    this.errorHandler.errorMessage$.subscribe(message => {
      this.errorMessage = message;
    });
  }

  setMenuStatus(open: boolean) {
    this.isMenuOpen = open;
  }
}
