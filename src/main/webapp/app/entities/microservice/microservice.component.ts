import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from './microservice.service';
import { MicroserviceDeleteDialogComponent } from './microservice-delete-dialog.component';

@Component({
  selector: 'jhi-microservice',
  templateUrl: './microservice.component.html',
})
export class MicroserviceComponent implements OnInit, OnDestroy {
  microservices?: IMicroservice[];
  eventSubscriber?: Subscription;

  constructor(
    protected microserviceService: MicroserviceService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.microserviceService.query().subscribe((res: HttpResponse<IMicroservice[]>) => (this.microservices = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMicroservices();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMicroservice): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInMicroservices(): void {
    this.eventSubscriber = this.eventManager.subscribe('microserviceListModification', () => this.loadAll());
  }

  delete(microservice: IMicroservice): void {
    const modalRef = this.modalService.open(MicroserviceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.microservice = microservice;
  }
}
