import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ReleaseGraphComponent } from './release-graph.component';

@NgModule({
  declarations: [ReleaseGraphComponent],
  imports: [MicrocatalogSharedModule],
  exports: [ReleaseGraphComponent],
})
export class ReleaseGraphModule {}
