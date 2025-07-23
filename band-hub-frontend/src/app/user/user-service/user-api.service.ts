import { Injectable } from '@angular/core';
import {catchError, Observable, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {AccountResponse, AccountUpdateRequest} from '../../core/models/account.model';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  private apiURL = 'http://localhost:9000/account';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getCurrentUser(): Observable<AccountResponse>{
    return this.http.get<AccountResponse>(`${this.apiURL}/current`, { withCredentials: true })
      .pipe(
        catchError((error) =>
          this.errorHandler.handleError(error)),
        tap()
      );
  }

  updateUserData(user: AccountUpdateRequest): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(`${this.apiURL}`, user, { withCredentials: true })
      .pipe(
        catchError((err) =>
          this.errorHandler.handleError(err))
      );
  }

  checkPassword(password: string): Observable<boolean> {
    return this.http.post<boolean>(`${this.apiURL}/password`, password, { withCredentials: true })
      .pipe(
        catchError((error) =>
        this.errorHandler.handleError(error))
      );
  }

  changePassword(password: string): Observable<void> {
    return this.http.patch<void>(`${this.apiURL}/password`, password, { withCredentials: true })
      .pipe(
        catchError((error) =>
        this.errorHandler.handleError(error))
      );
  }
}
