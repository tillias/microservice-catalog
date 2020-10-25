import { NgModule } from '@angular/core';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ReleasePathComponent } from './release-path.component';

@NgModule({
  declarations: [ReleasePathComponent],
  imports: [MicrocatalogSharedModule],
  exports: [ReleasePathComponent],
})
export class ReleasePathModule {}
