import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing-footer',
  imports: [RouterLink],
  templateUrl: './landing-footer.component.html',
  styleUrl: './landing-footer.component.scss'
})
/** Pie de página mostrado en la landing page. */
export class LandingFooterComponent {}
