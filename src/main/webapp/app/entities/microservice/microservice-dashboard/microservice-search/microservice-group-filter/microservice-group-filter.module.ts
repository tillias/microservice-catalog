import { NgModule } from '@angular/core';
import { MicroserviceGroupFilterComponent } from './microservice-group-filter.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';

@NgModule({
  declarations: [MicroserviceGroupFilterComponent],
  imports: [MicrocatalogSharedModule],
  exports: [MicroserviceGroupFilterComponent],
})
export class MicroserviceGroupFilterModule {}
