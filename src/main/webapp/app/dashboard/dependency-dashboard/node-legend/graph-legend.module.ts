import { NgModule } from '@angular/core';
import { GraphLegendComponent } from './graph-legend.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { VertexLegendComponent } from './vertex-legend/vertex-legend.component';
import { EdgeLegendComponent } from './edge-legend/edge-legend.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [GraphLegendComponent, VertexLegendComponent, EdgeLegendComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [GraphLegendComponent],
})
export class GraphLegendModule {}
