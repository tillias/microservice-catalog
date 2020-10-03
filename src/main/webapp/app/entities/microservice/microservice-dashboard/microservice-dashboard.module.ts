import { NgModule } from '@angular/core';
import { MicroserviceDashboardComponent } from './microservice-dashboard.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [MicroserviceDashboardComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [MicroserviceDashboardComponent],
})
export class MicroserviceDashboardModule {}
