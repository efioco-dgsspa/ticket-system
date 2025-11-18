// src/app/services/loader.service.ts
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class LoaderService {
  private _loading = new BehaviorSubject<boolean>(false);
  public loading$ = this._loading.asObservable();

  private requestCount = 0;

  show() {
    this.requestCount++;
    console.log(`[LoaderService] show() called (count=${this.requestCount})`);
    if (this.requestCount === 1) {
      this._loading.next(true);
    }
  }

  hide() {
    if (this.requestCount > 0) {
      this.requestCount--;
      console.log(`[LoaderService] hide() called (count=${this.requestCount})`);
    }
    if (this.requestCount === 0) {
      this._loading.next(false);
    }
  }
}
