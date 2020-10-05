import { NgModule } from '@angular/core';
import { DependencyDashboardComponent } from './dependency-dashboard.component';
import { RouterModule } from '@angular/router';
import { dependencyDashboardRoute } from './dependency-dashboard.route';
import { MicrocatalogSharedModule } from '../../shared/shared.module';
import { NgxGraphModule } from '@swimlane/ngx-graph';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [MicrocatalogSharedModule, NgxGraphModule, BrowserAnimationsModule, RouterModule.forChild(dependencyDashboardRoute)],
  declarations: [DependencyDashboardComponent],
})
export class DependencyDashboardModule {}
