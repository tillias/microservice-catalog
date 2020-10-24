import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReleaseGroup } from 'app/shared/model/release-group.model';
import { ReleaseGroupService } from './release-group.service';

@Component({
  templateUrl: './release-group-delete-dialog.component.html',
})
export class ReleaseGroupDeleteDialogComponent {
  releaseGroup?: IReleaseGroup;

  constructor(
    protected releaseGroupService: ReleaseGroupService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releaseGroupService.delete(id).subscribe(() => {
      this.eventManager.broadcast('releaseGroupListModification');
      this.activeModal.close();
    });
  }
}
