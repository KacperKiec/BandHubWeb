import { Injectable } from '@angular/core';
import {catchError, Observable, tap} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Band, Musician, Page} from '../../core/models/types';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';

@Injectable({
  providedIn: 'root'
})
export class MusicianApiService {

  private apiURL = 'http://localhost:9000/musician'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getMusicians(page: number = 0, size: number = 10): Observable<Page<Musician>> {
    return this.http.get<Page<Musician>>(`${this.apiURL}/all?page=${page}&size=${size}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getMusicianById(id: number): Observable<Musician> {
    return this.http.get<Musician>(`${this.apiURL}/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getMusicianBands(id: number): Observable<Band[]> {
    return this.http.get<Band[]>(`${this.apiURL}/${id}/bands`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getMusiciansWithParams(filters: {name: string, value: string[]}[], page: number = 0, size: number = 10): Observable<Page<Musician>> {
    if (filters.length === 0) {
      return this.getMusicians(page, size);
    }

    let params = new HttpParams();

    for(let filter of filters) {
      for (let val of filter.value) {
        params = params.append(filter.name, val);
      }
    }

    return this.http.get<Page<Musician>>(`${this.apiURL}/search?page=${page}&size=${size}`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  //addMusician()
}
