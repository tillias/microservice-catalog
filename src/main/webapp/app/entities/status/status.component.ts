import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from './status.service';
import { StatusDeleteDialogComponent } from './status-delete-dialog.component';

@Component({
  selector: 'jhi-status',
  templateUrl: './status.component.html',
})
export class StatusComponent implements OnInit, OnDestroy {
  statuses?: IStatus[];
  eventSubscriber?: Subscription;

  constructor(protected statusService: StatusService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.statusService.query().subscribe((res: HttpResponse<IStatus[]>) => (this.statuses = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInStatuses();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IStatus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInStatuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('statusListModification', () => this.loadAll());
  }

  delete(status: IStatus): void {
    const modalRef = this.modalService.open(StatusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.status = status;
  }
}
