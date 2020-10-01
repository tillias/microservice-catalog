import { NgModule } from '@angular/core';
import { MicroserviceDashboardComponent } from './microservice-dashboard.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';

@NgModule({
  declarations: [MicroserviceDashboardComponent],
  imports: [MicrocatalogSharedModule],
  exports: [MicroserviceDashboardComponent],
})
export class MicroserviceDashboardModule {}
