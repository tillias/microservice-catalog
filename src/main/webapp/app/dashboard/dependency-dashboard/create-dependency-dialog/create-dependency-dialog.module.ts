import { NgModule } from '@angular/core';
import { CreateDependencyDialogComponent } from './create-dependency-dialog.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';
import { MicroserviceSearchModule } from '../../../entities/microservice/microservice-dashboard/microservice-search/microservice-search.module';

@NgModule({
  imports: [MicroserviceSearchModule, MicrocatalogSharedModule],
  declarations: [CreateDependencyDialogComponent],
})
export class CreateDependencyDialogModule {}
