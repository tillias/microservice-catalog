import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { MicroserviceDashboardModule } from 'app/entities/microservice/microservice-dashboard/microservice-dashboard.module';

@NgModule({
  imports: [MicrocatalogSharedModule, MicroserviceDashboardModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
})
export class MicrocatalogHomeModule {}
