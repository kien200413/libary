import { bootstrapApplication } from '@angular/platform-browser';

import { provideRouter } from '@angular/router';
import { appRoutes } from './app/app.routes';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { DetailComponent } from './app/component/detail/detail.component';
import { AppComponent } from './app/app/app.component';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(appRoutes),
    importProvidersFrom(HttpClientModule)
  ]
}).catch(err => console.error(err));
