import { NgModule } from '@angular/core';
import { MicroserviceSearchComponent } from './microservice-search.component';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [MicroserviceSearchComponent],
  imports: [MicrocatalogSharedModule, RouterModule],
  exports: [MicroserviceSearchComponent],
})
export class MicroserviceSearchModule {}
