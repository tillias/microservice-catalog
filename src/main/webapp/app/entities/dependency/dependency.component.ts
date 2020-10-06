import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDependency } from 'app/shared/model/dependency.model';
import { DependencyService } from './dependency.service';
import { DependencyDeleteDialogComponent } from './dependency-delete-dialog.component';

@Component({
  selector: 'jhi-dependency',
  templateUrl: './dependency.component.html',
})
export class DependencyComponent implements OnInit, OnDestroy {
  dependencies?: IDependency[];
  eventSubscriber?: Subscription;

  constructor(
    protected dependencyService: DependencyService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.dependencyService.query().subscribe((res: HttpResponse<IDependency[]>) => (this.dependencies = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDependencies();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDependency): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInDependencies(): void {
    this.eventSubscriber = this.eventManager.subscribe('dependencyListModification', () => this.loadAll());
  }

  delete(dependency: IDependency): void {
    const modalRef = this.modalService.open(DependencyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dependency = dependency;
  }
}
