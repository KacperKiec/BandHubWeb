import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {

  private loadingSubject = new BehaviorSubject<boolean>(false);
  loading$ = this.loadingSubject.asObservable();

  private loadingCount = 0;

  show() {
    this.loadingCount++;
    if (this.loadingCount === 1) {
      this.loadingSubject.next(true);
    }
  }

  hide() {
    if (this.loadingCount > 0) {
      this.loadingCount--;
    }
    if (this.loadingCount === 0) {
      this.loadingSubject.next(false);
    }
  }
}
