import { Injectable } from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {AccountResponse} from '../../core/models/account.model';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {

  private apiURL = 'http://localhost:9000'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getCurrentUser(): Observable<AccountResponse>{
    return this.http.get<AccountResponse>(`${this.apiURL}/account/current`, { withCredentials: true })
      .pipe(
        catchError((error) =>
          this.errorHandler.handleError(error))
      );
  }
}
