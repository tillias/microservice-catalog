import { NgModule } from '@angular/core';
import { MicroserviceDashboardComponent } from './microservice-dashboard.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';
import { RouterModule } from '@angular/router';
import { MicroserviceCardComponent } from './microservice-card/microservice-card.component';
import { MicroserviceSearchComponent } from './microservice-search/microservice-search.component';

@NgModule({
  declarations: [MicroserviceDashboardComponent, MicroserviceCardComponent, MicroserviceSearchComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [MicroserviceDashboardComponent],
})
export class MicroserviceDashboardModule {}
