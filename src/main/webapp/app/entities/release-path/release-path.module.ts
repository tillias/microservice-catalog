import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ReleasePathComponent } from './release-path.component';
import { ReleasePathDetailComponent } from './release-path-detail.component';
import { ReleasePathUpdateComponent } from './release-path-update.component';
import { ReleasePathDeleteDialogComponent } from './release-path-delete-dialog.component';
import { releasePathRoute } from './release-path.route';

@NgModule({
  imports: [MicrocatalogSharedModule, RouterModule.forChild(releasePathRoute)],
  declarations: [ReleasePathComponent, ReleasePathDetailComponent, ReleasePathUpdateComponent, ReleasePathDeleteDialogComponent],
  entryComponents: [ReleasePathDeleteDialogComponent],
})
export class MicrocatalogReleasePathModule {}
