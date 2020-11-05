import { NgModule } from '@angular/core';
import { GraphLegendComponent } from './graph-legend.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';
import { VertexLegendComponent } from './vertex-legend/vertex-legend.component';
import { EdgeLegendComponent } from './edge-legend/edge-legend.component';

@NgModule({
  declarations: [GraphLegendComponent, VertexLegendComponent, EdgeLegendComponent],
  imports: [MicrocatalogSharedModule],
  exports: [GraphLegendComponent],
})
export class GraphLegendModule {}
