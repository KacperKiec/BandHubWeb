import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {Account} from '../../core/models/types';
import {catchError, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {

  private apiURL = 'http://localhost:9000'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  registerAccount(account: Account): Observable<void> {
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
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.apiURL}/auth/logout`, {}, { withCredentials: true })
      .pipe(
        catchError((error) =>
          this.errorHandler.handleError(error))
    );
  }
}
