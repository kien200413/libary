import { bootstrapApplication } from '@angular/platform-browser';
import { HomeComponent } from './app/component/home/home.component';
import { OderComponent } from './app/component/oder/oder.component';
import { config } from './app/app.config.server';
import { DetailComponent } from './app/component/detail/detail.component';
import { OrderConfirmComponent } from './app/component/oder-confirm/oder-confirm.component';
import { LoginComponent } from './app/component/login/login.component';
import { RegisterComponent } from './app/component/register/register.component';
import { AppComponent } from './app/app/app.component';
import { ProductDetailComponent } from './app/detilproduct/detilproduct.component';

const bootstrap = () => bootstrapApplication(AppComponent, config);

export default bootstrap;
