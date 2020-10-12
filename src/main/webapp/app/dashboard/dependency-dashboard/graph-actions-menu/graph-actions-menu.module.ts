import { NgModule } from '@angular/core';
import { GraphActionsMenuComponent } from './graph-actions-menu.component';
import { MicrocatalogSharedModule } from '../../../shared/shared.module';

@NgModule({
  declarations: [GraphActionsMenuComponent],
  imports: [MicrocatalogSharedModule],
  exports: [GraphActionsMenuComponent],
})
export class GraphActionsMenuModule {}
