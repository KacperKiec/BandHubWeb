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
    const backendMessage = error.error?.message || error.message;
    const errorCode = error.error?.errorCode || statusCode;

    const fullErrorMessage = `Błąd: ${backendMessage} (kod: ${errorCode})`;
    //alert(fullErrorMessage);

    this.errorSubject.next(fullErrorMessage);

    return throwError(() => fullErrorMessage);
  }


}
