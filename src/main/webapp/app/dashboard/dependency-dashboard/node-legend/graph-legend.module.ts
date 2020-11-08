import { NgModule } from '@angular/core';
import { GraphLegendComponent } from './graph-legend.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { VertexLegendComponent } from './vertex-legend/vertex-legend.component';
import { EdgeLegendComponent } from './edge-legend/edge-legend.component';
import { RouterModule } from '@angular/router';
import { CoordinatesE2eComponent } from './coordinates-e2e/coordinates-e2e.component';

@NgModule({
  declarations: [GraphLegendComponent, VertexLegendComponent, EdgeLegendComponent, CoordinatesE2eComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [GraphLegendComponent],
})
export class GraphLegendModule {}
