import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {AuthApiService} from '../auth-service/auth-api.service';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedIn = new BehaviorSubject<boolean>(false);

  isLoggedIn$ = this.loggedIn.asObservable();

  constructor(private authApiService: AuthApiService, private router: Router) {
  }

  setLoginStatus(status: boolean): void {
    this.loggedIn.next(status);
  }

  checkLoginStatus() {
    this.authApiService.checkLoginStatus().subscribe({
      next: (resp) => {
        this.setLoginStatus(resp);
        if (!resp) {
          localStorage.clear();
          this.router.navigate(['/home']);
        }
      },
      error: () => {
        localStorage.clear();
        this.setLoginStatus(false);
        this.router.navigate(['/home']);
      }

    });
  }
}
