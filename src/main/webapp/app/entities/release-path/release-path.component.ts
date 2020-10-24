import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReleasePath } from 'app/shared/model/release-path.model';
import { ReleasePathService } from './release-path.service';
import { ReleasePathDeleteDialogComponent } from './release-path-delete-dialog.component';

@Component({
  selector: 'jhi-release-path',
  templateUrl: './release-path.component.html',
})
export class ReleasePathComponent implements OnInit, OnDestroy {
  releasePaths?: IReleasePath[];
  eventSubscriber?: Subscription;

  constructor(
    protected releasePathService: ReleasePathService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.releasePathService.query().subscribe((res: HttpResponse<IReleasePath[]>) => (this.releasePaths = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInReleasePaths();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IReleasePath): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInReleasePaths(): void {
    this.eventSubscriber = this.eventManager.subscribe('releasePathListModification', () => this.loadAll());
  }

  delete(releasePath: IReleasePath): void {
    const modalRef = this.modalService.open(ReleasePathDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.releasePath = releasePath;
  }
}
