import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ImpactAnalysisLegendComponent } from './impact-analysis-legend.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [ImpactAnalysisLegendComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [ImpactAnalysisLegendComponent],
})
export class ImpactAnalysisLegendModule {}
