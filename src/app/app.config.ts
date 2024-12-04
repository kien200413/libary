import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, HTTP_INTERCEPTORS } from '@angular/common/http'; // Sửa ở đây
import { TokenInterceptor } from './interceptors/token.interceptors';
import { appRoutes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(appRoutes), 
    provideClientHydration(),
    provideHttpClient(),  // Đăng ký HttpClient tại đây

    {
      provide: HTTP_INTERCEPTORS,  // Sửa ở đây
      useClass: TokenInterceptor,
      multi: true // Cho phép nhiều interceptor
    }
  ]
};
