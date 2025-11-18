// src/app/interceptors/loader.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoaderService } from '../services/loader.service';

export const loaderInterceptor: HttpInterceptorFn = (req, next) => {
  const loaderService = inject(LoaderService);
  console.log('[LoaderInterceptor] Intercepting:', req.method, req.url);
  
  loaderService.show();
  const startTime = Date.now();

  return next(req).pipe(
    finalize(() => {
      const elapsed = Date.now() - startTime;
      const remainingTime = Math.max(0, 1000 - elapsed); // Minimo 1 secondo

      setTimeout(() => {
        loaderService.hide();
      }, remainingTime);
    })
  );
};
