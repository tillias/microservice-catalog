import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReleaseStep } from 'app/shared/model/release-step.model';
import { ReleaseStepService } from './release-step.service';

@Component({
  templateUrl: './release-step-delete-dialog.component.html',
})
export class ReleaseStepDeleteDialogComponent {
  releaseStep?: IReleaseStep;

  constructor(
    protected releaseStepService: ReleaseStepService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releaseStepService.delete(id).subscribe(() => {
      this.eventManager.broadcast('releaseStepListModification');
      this.activeModal.close();
    });
  }
}
