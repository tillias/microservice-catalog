import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDependency } from 'app/shared/model/dependency.model';
import { DependencyService } from './dependency.service';

@Component({
  templateUrl: './dependency-delete-dialog.component.html',
})
export class DependencyDeleteDialogComponent {
  dependency?: IDependency;

  constructor(
    protected dependencyService: DependencyService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dependencyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('dependencyListModification');
      this.activeModal.close();
    });
  }
}
