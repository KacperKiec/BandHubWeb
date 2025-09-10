import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageApiService {

  private apiURL = 'http://localhost:9000/image';

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  saveImage(image: File, oldURL: string): Observable<{url: string}> {
    const formData = new FormData();
    formData.append('file', image);
    formData.append('oldURL', oldURL);

    return this.http.post<{url: string}>(this.apiURL, formData, { withCredentials: true })
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
