import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ReleaseGroupComponent } from './release-group.component';
import { ReleaseGroupDetailComponent } from './release-group-detail.component';
import { ReleaseGroupUpdateComponent } from './release-group-update.component';
import { ReleaseGroupDeleteDialogComponent } from './release-group-delete-dialog.component';
import { releaseGroupRoute } from './release-group.route';

@NgModule({
  imports: [MicrocatalogSharedModule, RouterModule.forChild(releaseGroupRoute)],
  declarations: [ReleaseGroupComponent, ReleaseGroupDetailComponent, ReleaseGroupUpdateComponent, ReleaseGroupDeleteDialogComponent],
  entryComponents: [ReleaseGroupDeleteDialogComponent],
})
export class MicrocatalogReleaseGroupModule {}
