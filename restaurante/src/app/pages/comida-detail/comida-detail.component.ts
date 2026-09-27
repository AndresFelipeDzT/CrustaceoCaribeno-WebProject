import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Adicional } from '../../models/adicional.model';
import { Comida } from '../../models/comida.model';
import { ComidaService } from '../../service/comida.service';

@Component({
  selector: 'app-comida-detail',
  imports: [CurrencyPipe, RouterLink],
  templateUrl: './comida-detail.component.html',
  styleUrl: './comida-detail.component.scss'
})
export class ComidaDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private comidaService = inject(ComidaService);
  comida: Comida | undefined;
  adicionales: Adicional[] = [];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.comida = this.comidaService.getComidaById(id);
    if (this.comida) {
      this.adicionales = this.comidaService.getAdicionalesPorCategoria(this.comida.categoria);
    }
  }
}
