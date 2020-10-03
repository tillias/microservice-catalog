import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from './status.service';

@Component({
  templateUrl: './status-delete-dialog.component.html',
})
export class StatusDeleteDialogComponent {
  status?: IStatus;

  constructor(protected statusService: StatusService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.statusService.delete(id).subscribe(() => {
      this.eventManager.broadcast('statusListModification');
      this.activeModal.close();
    });
  }
}
