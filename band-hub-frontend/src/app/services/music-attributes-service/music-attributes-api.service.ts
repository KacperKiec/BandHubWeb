import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {catchError, Observable} from 'rxjs';
import {Instrument} from '../../core/models/instrument.model';
import {Genre} from '../../core/models/genre.model';

@Injectable({
  providedIn: 'root'
})
export class MusicAttributesApiService {

  private apiURL = 'http://localhost:9000'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getInstruments(): Observable<Instrument[]> {
    return this.http.get<Instrument[]>(`${this.apiURL}/instrument`)
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getGenres(): Observable<Genre[]> {
    return this.http.get<Genre[]>(`${this.apiURL}/genre`)
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
