import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { releasePathDashboardRoute } from './release-path-dashboard.route';
import { MicrocatalogSharedModule } from '../../shared/shared.module';
import { ReleasePathDashboardComponent } from './release-path-dashboard.component';
import { ReleasePathModule } from './release-path/release-path.module';
import { ReleaseGraphModule } from './release-graph/release-graph.module';

@NgModule({
  imports: [ReleasePathModule, ReleaseGraphModule, MicrocatalogSharedModule, RouterModule.forChild(releasePathDashboardRoute)],
  declarations: [ReleasePathDashboardComponent],
})
export class ReleasePathDashboardModule {}
