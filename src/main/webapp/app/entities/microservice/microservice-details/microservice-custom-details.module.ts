import { NgModule } from '@angular/core';
import { MicroserviceCustomDetailsComponent } from './microservice-custom-details.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [MicroserviceCustomDetailsComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [MicroserviceCustomDetailsComponent],
})
export class MicroserviceCustomDetailsModule {}
