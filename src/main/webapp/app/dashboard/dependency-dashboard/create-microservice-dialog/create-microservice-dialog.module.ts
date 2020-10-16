import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { CreateMicroserviceDialogComponent } from './create-microservice-dialog.component';

@NgModule({
  declarations: [CreateMicroserviceDialogComponent],
  imports: [MicrocatalogSharedModule],
  exports: [CreateMicroserviceDialogComponent],
})
export class CreateMicroserviceDialogModule {}
