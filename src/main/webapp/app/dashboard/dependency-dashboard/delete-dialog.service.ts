import { Injectable } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DependencyDeleteDialogComponent } from '../../entities/dependency/dependency-delete-dialog.component';
import { IDependency } from '../../shared/model/dependency.model';
import { IMicroservice } from '../../shared/model/microservice.model';
import { MicroserviceDeleteDialogComponent } from '../../entities/microservice/microservice-delete-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class DeleteDialogService {
  private isDependencyOpen = false;
  private isMicroserviceOpen = false;

  constructor(private modalService: NgbModal) {}

  openForDependency(dependency?: IDependency): void {
    if (this.isDependencyOpen) {
      return;
    }
    this.isDependencyOpen = true;

    const modalRef: NgbModalRef = this.modalService.open(DependencyDeleteDialogComponent, this.getOptions());
    modalRef.componentInstance.dependency = dependency;
    modalRef.result.finally(() => (this.isDependencyOpen = false));
  }

  openForMicroservice(microservice?: IMicroservice): void {
    if (this.isMicroserviceOpen) {
      return;
    }
    this.isMicroserviceOpen = true;

    const modalRef: NgbModalRef = this.modalService.open(MicroserviceDeleteDialogComponent, this.getOptions());
    modalRef.componentInstance.microservice = microservice;
    modalRef.result.finally(() => (this.isMicroserviceOpen = false));
  }

  getOptions(): NgbModalOptions {
    return {
      centered: true,
    };
  }
}
