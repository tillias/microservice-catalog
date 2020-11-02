import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ImpactAnalysisLegendComponent } from './impact-analysis-legend.component';

@NgModule({
  declarations: [ImpactAnalysisLegendComponent],
  imports: [MicrocatalogSharedModule],
  exports: [ImpactAnalysisLegendComponent],
})
export class ImpactAnalysisLegendModule {}
