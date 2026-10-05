import { Component } from '@angular/core';
import { LandingNavbarComponent } from '../../components/navbar/landing-navbar/landing-navbar.component';
import { LandingFooterComponent } from '../../components/footer/landing-footer/landing-footer.component';
import { HeroBannerComponent } from './components/hero-banner/hero-banner.component';
import { PrimeraSeccionComponent } from './components/primera-seccion/primera-seccion.component';
import { SegundaSeccionComponent } from './components/segunda-seccion/segunda-seccion.component';
import { TerceraSeccionComponent } from './components/tercera-seccion/tercera-seccion.component';

@Component({
  selector: 'app-home',
  imports: [LandingNavbarComponent, HeroBannerComponent, PrimeraSeccionComponent, SegundaSeccionComponent, TerceraSeccionComponent, LandingFooterComponent],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.scss'
})
export class HomePageComponent {}
