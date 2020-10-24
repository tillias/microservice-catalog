import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleaseStep } from 'app/shared/model/release-step.model';
import { ReleaseStepService } from './release-step.service';
import { ReleaseStepDeleteDialogComponent } from './release-step-delete-dialog.component';

@Component({
  selector: 'jhi-release-step',
  templateUrl: './release-step.component.html',
})
export class ReleaseStepComponent implements OnInit, OnDestroy {
  releaseSteps?: IReleaseStep[];
  eventSubscriber?: Subscription;

  constructor(
    protected releaseStepService: ReleaseStepService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.releaseStepService.query().subscribe((res: HttpResponse<IReleaseStep[]>) => (this.releaseSteps = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReleaseSteps();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReleaseStep): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReleaseSteps(): void {
    this.eventSubscriber = this.eventManager.subscribe('releaseStepListModification', () => this.loadAll());
  }

  delete(releaseStep: IReleaseStep): void {
    const modalRef = this.modalService.open(ReleaseStepDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.releaseStep = releaseStep;
  }
}
