import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { MicroserviceComponent } from './microservice.component';
import { MicroserviceDetailComponent } from './microservice-detail.component';
import { MicroserviceUpdateComponent } from './microservice-update.component';
import { MicroserviceDeleteDialogComponent } from './microservice-delete-dialog.component';
import { microserviceRoute } from './microservice.route';

@NgModule({
  imports: [MicrocatalogSharedModule, RouterModule.forChild(microserviceRoute)],
  declarations: [MicroserviceComponent, MicroserviceDetailComponent, MicroserviceUpdateComponent, MicroserviceDeleteDialogComponent],
  entryComponents: [MicroserviceDeleteDialogComponent],
})
export class MicrocatalogMicroserviceModule {}
