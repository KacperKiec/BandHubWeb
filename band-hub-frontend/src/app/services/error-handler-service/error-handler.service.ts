import { Injectable } from '@angular/core';
import {Observable, Subject, throwError} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';
import {ErrorResponse} from '../../core/models/types';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  errorSubject = new Subject<string>();
  observableError = this.errorSubject.asObservable();

  handleError(error: HttpErrorResponse): Observable<never> {
    const statusCode = error.status;
    const message = error.message;

    const fullErrorMessage = `Błąd ${statusCode}: ${message}`;
    this.errorSubject.next(fullErrorMessage);

    return throwError(() => new Error(fullErrorMessage));
  }

}
