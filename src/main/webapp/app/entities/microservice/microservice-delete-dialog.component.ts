import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from './microservice.service';

@Component({
  templateUrl: './microservice-delete-dialog.component.html',
})
export class MicroserviceDeleteDialogComponent {
  microservice?: IMicroservice;

  constructor(
    protected microserviceService: MicroserviceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.microserviceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('microserviceListModification');
      this.activeModal.close();
    });
  }
}
