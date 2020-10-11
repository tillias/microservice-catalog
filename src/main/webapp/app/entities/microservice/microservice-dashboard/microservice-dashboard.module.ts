import { NgModule } from '@angular/core';
import { MicroserviceDashboardComponent } from './microservice-dashboard.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';
import { MicroserviceCardComponent } from './microservice-card/microservice-card.component';
import { MicroserviceSearchModule } from 'app/entities/microservice/microservice-dashboard/microservice-search/microservice-search.module';
import { MicroserviceGroupFilterPipe } from './microservice-group-filter.pipe';

@NgModule({
  declarations: [MicroserviceDashboardComponent, MicroserviceCardComponent, MicroserviceGroupFilterPipe],
  imports: [MicroserviceSearchModule, MicrocatalogSharedModule, RouterModule],
  exports: [MicroserviceDashboardComponent],
})
export class MicroserviceDashboardModule {}
