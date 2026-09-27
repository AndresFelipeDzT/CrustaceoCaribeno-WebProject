import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-menu-hero-banner',
  imports: [],
  templateUrl: './hero-banner.component.html',
  styleUrl: './hero-banner.component.scss'
})
export class MenuHeroBannerComponent {
  @Input() menuAnchor = 'menu-tarjetas';
}
