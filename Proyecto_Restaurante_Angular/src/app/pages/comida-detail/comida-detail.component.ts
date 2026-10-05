import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Comida } from '../../models/comida.model';
import { ComidaService } from '../../service/comida.service';
import { ComidaDetailNavbarComponent } from './components/comida-detail-navbar/comida-detail-navbar.component';
import { ComidaPlatoComponent } from './components/comida-plato/comida-plato.component';
import { ComidaAcompanamientosComponent } from './components/comida-acompanamientos/comida-acompanamientos.component';

@Component({
  selector: 'app-comida-detail',
  imports: [RouterLink, ComidaDetailNavbarComponent, ComidaPlatoComponent, ComidaAcompanamientosComponent],
  templateUrl: './comida-detail.component.html',
  styleUrl: './comida-detail.component.scss'
})
export class ComidaDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private comidaService = inject(ComidaService);
  comida: Comida | undefined;

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.comida = this.comidaService.getComidaById(id);
  }
}
