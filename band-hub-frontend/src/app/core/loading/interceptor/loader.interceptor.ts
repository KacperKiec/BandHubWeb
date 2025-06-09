import { HttpInterceptorFn } from '@angular/common/http';
import {finalize} from 'rxjs';
import {inject} from '@angular/core';
import {LoaderService} from '../service/loader.service';

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoaderService);
  const excludePattern = /^http:\/\/localhost:9000\/notification\/.*$/;

  const shouldShowLoader = !excludePattern.test(req.url);

  if (shouldShowLoader) {
    loadingService.show();
  }

  return next(req).pipe(
    finalize(() => {
      console.log('Loader hide for:', req.url);
      loadingService.hide();
    })
  );
};
