import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {catchError, Observable, of, retry, tap} from 'rxjs';
import {AccountRegisterRequest} from '../../core/models/account.model';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {

  private apiURL = 'http://localhost:9000'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  registerAccount(account: AccountRegisterRequest): Observable<void> {
    const url = `${this.apiURL}/account`;

    return this.http.post<void>(url, account)
      .pipe(
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }

  login(email: string, password: string): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/auth/login`, { email, password }, { withCredentials: true })
      .pipe(
        tap(() => {
          this.getCurrentUser().subscribe(id => {
            localStorage.setItem('userId', JSON.stringify(id));
          });
        }),
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/auth/logout`, {}, { withCredentials: true })
      .pipe(
        tap(() => {
          localStorage.clear();
        }),
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }

  checkLoginStatus(): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiURL}/auth/check`, { withCredentials: true })
      .pipe(
        retry(2),
        catchError((error) => {
          localStorage.clear();
          return of(false);
        })
      );
  }

  getCurrentUser(): Observable<number> {
    return this.http.get<number>(`${this.apiURL}/auth/me`, { withCredentials: true })
      .pipe(
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }
}
