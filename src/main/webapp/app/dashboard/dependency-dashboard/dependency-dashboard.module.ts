import { NgModule } from '@angular/core';
import { DependencyDashboardComponent } from './dependency-dashboard.component';
import { RouterModule } from '@angular/router';
import { dependencyDashboardRoute } from './dependency-dashboard.route';
import { MicrocatalogSharedModule } from '../../shared/shared.module';
import { MicroserviceSearchModule } from '../../entities/microservice/microservice-dashboard/microservice-search/microservice-search.module';
import { GraphActionsMenuModule } from './graph-actions-menu/graph-actions-menu.module';
import { GraphLegendModule } from './node-legend/graph-legend.module';

@NgModule({
  imports: [
    MicroserviceSearchModule,
    MicrocatalogSharedModule,
    GraphActionsMenuModule,
    GraphLegendModule,
    RouterModule.forChild(dependencyDashboardRoute),
  ],
  declarations: [DependencyDashboardComponent],
})
export class DependencyDashboardModule {}
