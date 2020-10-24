import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleaseGroup } from 'app/shared/model/release-group.model';
import { ReleaseGroupService } from './release-group.service';
import { ReleaseGroupDeleteDialogComponent } from './release-group-delete-dialog.component';

@Component({
  selector: 'jhi-release-group',
  templateUrl: './release-group.component.html',
})
export class ReleaseGroupComponent implements OnInit, OnDestroy {
  releaseGroups?: IReleaseGroup[];
  eventSubscriber?: Subscription;

  constructor(
    protected releaseGroupService: ReleaseGroupService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.releaseGroupService.query().subscribe((res: HttpResponse<IReleaseGroup[]>) => (this.releaseGroups = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReleaseGroups();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReleaseGroup): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReleaseGroups(): void {
    this.eventSubscriber = this.eventManager.subscribe('releaseGroupListModification', () => this.loadAll());
  }

  delete(releaseGroup: IReleaseGroup): void {
    const modalRef = this.modalService.open(ReleaseGroupDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.releaseGroup = releaseGroup;
  }
}
