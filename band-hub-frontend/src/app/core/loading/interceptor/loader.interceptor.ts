import { HttpInterceptorFn } from '@angular/common/http';
import {finalize} from 'rxjs';
import {inject} from '@angular/core';
import {LoaderService} from '../service/loader.service';

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoaderService);
  loadingService.show();

  return next(req).pipe(
    finalize(() => loadingService.hide())
  );
};
