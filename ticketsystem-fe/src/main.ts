import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app/app';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { tokenInterceptor } from './app/services/auth/token-interceptor';
import { loaderInterceptor } from './app/interceptors/loader-interceptor';

bootstrapApplication(App, {
  providers: [
    provideRouter(routes),
    provideHttpClient(
      withInterceptors(
        [loaderInterceptor, tokenInterceptor]
      )
    ),
  ]
}).catch(err => console.error(err));
