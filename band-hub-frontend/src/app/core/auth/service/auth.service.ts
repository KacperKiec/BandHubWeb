import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {AuthApiService} from '../../../services/auth-service/auth-api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loggedIn = new BehaviorSubject<boolean>(false);

  isLoggedIn$ = this.loggedIn.asObservable();

  constructor(private authApiService: AuthApiService) {
  }

  setLoginStatus(status: boolean): void {
    this.loggedIn.next(status);
  }

  checkLoginStatus() {
    this.authApiService.checkLoginStatus().subscribe({
      next: (resp) => {
        this.setLoginStatus(resp);
      },
      error: () => {
        this.setLoginStatus(false);
      }
    });
  }
}
