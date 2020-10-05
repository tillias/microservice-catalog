import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { DependencyComponent } from './dependency.component';
import { DependencyDetailComponent } from './dependency-detail.component';
import { DependencyUpdateComponent } from './dependency-update.component';
import { DependencyDeleteDialogComponent } from './dependency-delete-dialog.component';
import { dependencyRoute } from './dependency.route';

@NgModule({
  imports: [MicrocatalogSharedModule, RouterModule.forChild(dependencyRoute)],
  declarations: [DependencyComponent, DependencyDetailComponent, DependencyUpdateComponent, DependencyDeleteDialogComponent],
  entryComponents: [DependencyDeleteDialogComponent],
})
export class MicrocatalogDependencyModule {}
