import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ImpactAnalysisDashboardComponent } from './impact-analysis-dashboard.component';
import { RouterModule } from '@angular/router';
import { impactAnalysisDashboardRoute } from './impact-analysis-dashboard.route';
import { ImpactAnalysisGraphModule } from './impact-analysis-graph/impact-analysis-graph.module';
import { ImpactAnalysisLegendModule } from './impact-analysis-legend/impact-analysis-legend.module';

@NgModule({
  imports: [
    MicrocatalogSharedModule,
    ImpactAnalysisGraphModule,
    ImpactAnalysisLegendModule,
    RouterModule.forChild(impactAnalysisDashboardRoute),
  ],
  declarations: [ImpactAnalysisDashboardComponent],
})
export class ImpactAnalysisDashboardModule {}
