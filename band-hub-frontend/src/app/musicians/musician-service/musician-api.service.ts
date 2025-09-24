import { Injectable } from '@angular/core';
import {catchError, Observable, tap} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../../core/error-handler-service/error-handler.service';
import {Musician, MusicianRequest} from '../../core/models/musician.model';
import {Page} from '../../core/models/pagination.model';
import {Band} from '../../core/models/band.model';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MusicianApiService {

  private apiURL = environment.apiUrl + '/musician'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getMusicians(page: number = 0, size: number = 10): Observable<Page<Musician>> {
    return this.http.get<Page<Musician>>(`${this.apiURL}/public/all?page=${page}&size=${size}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getMusician() {
    return this.http.get<Musician>(`${this.apiURL}/me`, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
      )
  }

  getMusicianById(id: number): Observable<Musician> {
    return this.http.get<Musician>(`${this.apiURL}/public/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getMusicianBands(id: number): Observable<Band[]> {
    return this.http.get<Band[]>(`${this.apiURL}/public/${id}/bands`)
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

    return this.http.get<Page<Musician>>(`${this.apiURL}/public/search?page=${page}&size=${size}`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addMusician(musician: MusicianRequest): Observable<Musician> {
    return this.http.post<Musician>(`${this.apiURL}`, musician, {withCredentials: true})
      .pipe(
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateMusician(musician: MusicianRequest): Observable<void> {
    return this.http.put<void>(`${this.apiURL}`, musician, {withCredentials: true})
      .pipe(
        catchError(err => this.errorHandler.handleError(err))
      );
  }
}
