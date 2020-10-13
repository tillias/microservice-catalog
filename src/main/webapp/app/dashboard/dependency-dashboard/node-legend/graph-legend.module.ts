import { NgModule } from '@angular/core';
import { GraphLegendComponent } from './graph-legend.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';

@NgModule({
  declarations: [GraphLegendComponent],
  imports: [MicrocatalogSharedModule],
  exports: [GraphLegendComponent],
})
export class GraphLegendModule {}
