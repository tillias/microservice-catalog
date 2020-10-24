import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReleasePath } from 'app/shared/model/release-path.model';
import { ReleasePathService } from './release-path.service';

@Component({
  templateUrl: './release-path-delete-dialog.component.html',
})
export class ReleasePathDeleteDialogComponent {
  releasePath?: IReleasePath;

  constructor(
    protected releasePathService: ReleasePathService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.releasePathService.delete(id).subscribe(() => {
      this.eventManager.broadcast('releasePathListModification');
      this.activeModal.close();
    });
  }
}
