import { Injectable } from '@angular/core';
import { NgbModal, NgbModalOptions, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CreateDependencyDialogComponent } from './create-dependency-dialog.component';

@Injectable({ providedIn: 'root' })
export class CreateDependencyDialogService {
  private isOpen = false;

  constructor(private modalService: NgbModal) {}

  open(): void {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;

    const options: NgbModalOptions = {
      centered: true,
    };

    const modalRef: NgbModalRef = this.modalService.open(CreateDependencyDialogComponent, options);
    modalRef.result.finally(() => (this.isOpen = false));
  }
}
