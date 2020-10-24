import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { ReleaseStepComponent } from './release-step.component';
import { ReleaseStepDetailComponent } from './release-step-detail.component';
import { ReleaseStepUpdateComponent } from './release-step-update.component';
import { ReleaseStepDeleteDialogComponent } from './release-step-delete-dialog.component';
import { releaseStepRoute } from './release-step.route';

@NgModule({
  imports: [MicrocatalogSharedModule, RouterModule.forChild(releaseStepRoute)],
  declarations: [ReleaseStepComponent, ReleaseStepDetailComponent, ReleaseStepUpdateComponent, ReleaseStepDeleteDialogComponent],
  entryComponents: [ReleaseStepDeleteDialogComponent],
})
export class MicrocatalogReleaseStepModule {}
