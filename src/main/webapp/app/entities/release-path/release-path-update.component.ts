import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IReleasePath, ReleasePath } from 'app/shared/model/release-path.model';
import { ReleasePathService } from './release-path.service';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';

@Component({
  selector: 'jhi-release-path-update',
  templateUrl: './release-path-update.component.html',
})
export class ReleasePathUpdateComponent implements OnInit {
  isSaving = false;
  microservices: IMicroservice[] = [];

  editForm = this.fb.group({
    id: [],
    createdOn: [],
    target: [null, Validators.required],
  });

  constructor(
    protected releasePathService: ReleasePathService,
    protected microserviceService: MicroserviceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releasePath }) => {
      if (!releasePath.id) {
        const today = moment().startOf('day');
        releasePath.createdOn = today;
      }

      this.updateForm(releasePath);

      this.microserviceService.query().subscribe((res: HttpResponse<IMicroservice[]>) => (this.microservices = res.body || []));
    });
  }

  updateForm(releasePath: IReleasePath): void {
    this.editForm.patchValue({
      id: releasePath.id,
      createdOn: releasePath.createdOn ? releasePath.createdOn.format(DATE_TIME_FORMAT) : null,
      target: releasePath.target,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releasePath = this.createFromForm();
    if (releasePath.id !== undefined) {
      this.subscribeToSaveResponse(this.releasePathService.update(releasePath));
    } else {
      this.subscribeToSaveResponse(this.releasePathService.create(releasePath));
    }
  }

  private createFromForm(): IReleasePath {
    return {
      ...new ReleasePath(),
      id: this.editForm.get(['id'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value ? moment(this.editForm.get(['createdOn'])!.value, DATE_TIME_FORMAT) : undefined,
      target: this.editForm.get(['target'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleasePath>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IMicroservice): any {
    return item.id;
  }
}
