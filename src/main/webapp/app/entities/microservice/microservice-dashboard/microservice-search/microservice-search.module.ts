import { NgModule } from '@angular/core';
import { MicroserviceSearchComponent } from './microservice-search.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { MicroserviceGroupFilterModule } from 'app/entities/microservice/microservice-dashboard/microservice-search/microservice-group-filter/microservice-group-filter.module';

@NgModule({
  declarations: [MicroserviceSearchComponent],
  imports: [MicrocatalogSharedModule, MicroserviceGroupFilterModule],
  exports: [MicroserviceSearchComponent],
})
export class MicroserviceSearchModule {}
