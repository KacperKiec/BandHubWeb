import { Injectable } from '@angular/core';
import {Observable, Subject, throwError} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  errorSubject = new Subject<string>();
  observableError = this.errorSubject.asObservable();

  handleError(error: HttpErrorResponse): Observable<never> {
    const statusCode = error.status;
    let errorMessage = '';

    if (statusCode === 400 || Array.isArray(error.error)) {
      const errors = error.error as string[];
      errorMessage = `Błędy walidacji:\n${errors.join('\n')}`;
      alert(errorMessage);
    } else if (error.error?.message) {
      const backendMessage = error.error.message;
      const errorCode = error.error.errorCode || statusCode;
      errorMessage = `Błąd: ${backendMessage} (kod: ${errorCode})`;
    } else {
      errorMessage = `Błąd: ${error.message || 'Nieznany błąd'} (kod: ${statusCode})`;
    }

    this.errorSubject.next(errorMessage);
    return throwError(() => error);
  }


}
