import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IMicroservice } from '../../../shared/model/microservice.model';
import { DependencyService } from '../../../entities/dependency/dependency.service';
import { Dependency } from '../../../shared/model/dependency.model';
import { JhiEventManager } from 'ng-jhipster';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-create-dependency-dialog',
  templateUrl: './create-dependency-dialog.component.html',
})
export class CreateDependencyDialogComponent implements OnInit {
  source?: IMicroservice;
  target?: IMicroservice;
  name: string;
  isSaving = false;

  constructor(protected eventManager: JhiEventManager, protected activeModal: NgbActiveModal, public dependencyService: DependencyService) {
    this.name = 'Please specify source & target';
  }

  ngOnInit(): void {
    if (this.source) {
      this.updateName();
    }
  }

  createDependency(): void {
    if (this.source && this.target) {
      this.isSaving = true;

      this.dependencyService
        .create({
          ...new Dependency(),
          name: this.name,
          source: this.source,
          target: this.target,
        })
        .pipe(
          finalize(() => {
            this.isSaving = false;
            this.activeModal.close();
          })
        )
        .subscribe(() => {
          this.eventManager.broadcast('dependencyListModification');
        });
    }
  }

  cancel(): void {
    this.activeModal.dismiss('closed');
  }

  onSourceSelected(payload: IMicroservice): void {
    this.source = payload;
    this.updateName();
  }

  onTargetSelected(payload: IMicroservice): void {
    this.target = payload;
    this.updateName();
  }

  updateName(): void {
    this.name = `${this.source?.name} -> ${this.target?.name}`;
  }

  swap(): void {
    const tmp = this.target;
    this.target = this.source;
    this.source = tmp;

    this.updateName();
  }
}
