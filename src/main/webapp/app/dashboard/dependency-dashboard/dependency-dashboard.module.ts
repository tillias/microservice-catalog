import { NgModule } from '@angular/core';
import { DependencyDashboardComponent } from './dependency-dashboard.component';
import { RouterModule } from '@angular/router';
import { dependencyDashboardRoute } from './dependency-dashboard.route';
import { MicrocatalogSharedModule } from '../../shared/shared.module';
import { MicroserviceSearchModule } from '../../entities/microservice/microservice-dashboard/microservice-search/microservice-search.module';
import { GraphLegendModule } from './node-legend/graph-legend.module';
import { CreateDependencyDialogModule } from './create-dependency-dialog/create-dependency-dialog.module';

@NgModule({
  imports: [
    MicroserviceSearchModule,
    MicrocatalogSharedModule,
    GraphLegendModule,
    CreateDependencyDialogModule,
    RouterModule.forChild(dependencyDashboardRoute),
  ],
  declarations: [DependencyDashboardComponent],
})
export class DependencyDashboardModule {}
