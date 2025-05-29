import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ErrorHandlerService} from '../error-handler-service/error-handler.service';
import {catchError, Observable, tap} from 'rxjs';
import {Page} from '../../core/models/pagination.model';
import {Band, BandRequest, BandUpdateRequest} from '../../core/models/band.model';

@Injectable({
  providedIn: 'root'
})
export class BandApiService {

  private apiURL = 'http://localhost:9000/band'

  constructor(private http: HttpClient, private errorHandler: ErrorHandlerService) { }

  getBands(page: number = 0, size: number = 10): Observable<Page<Band>> {
    return this.http.get<Page<Band>>(`${this.apiURL}/all?page=${page}&size=${size}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBandById(id: number): Observable<Band> {
    return this.http.get<Band>(`${this.apiURL}/${id}`)
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  getBandsWithParams(filters: {name: string, value: string[]}[], page: number = 0, size: number = 10): Observable<Page<Band>> {
    if (filters.length === 0) {
      return this.getBands(page, size);
    }

    let params = new HttpParams();

    for(let filter of filters) {
      for (let val of filter.value) {
        params = params.append(filter.name, val);
      }
    }

    return this.http.get<Page<Band>>(`${this.apiURL}/search?page=${page}&size=${size}`, {params})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  addBand(band: BandRequest): Observable<Band> {
    return this.http.post<Band>(`${this.apiURL}`, band, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }

  updateBand(band: BandUpdateRequest): Observable<Band> {
    return this.http.put<Band>(`${this.apiURL}`, band, {withCredentials: true})
      .pipe(
        tap(() => this.errorHandler.errorSubject.next('')),
        catchError(error => this.errorHandler.handleError(error))
      );
  }
}
