import { NgModule } from '@angular/core';
import { ImpactAnalysisGraphComponent } from './impact-analysis-graph.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';

@NgModule({
  declarations: [ImpactAnalysisGraphComponent],
  imports: [MicrocatalogSharedModule],
  exports: [ImpactAnalysisGraphComponent],
})
export class ImpactAnalysisGraphModule {}
