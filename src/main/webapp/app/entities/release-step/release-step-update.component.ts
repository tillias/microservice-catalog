import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IReleaseStep, ReleaseStep } from 'app/shared/model/release-step.model';
import { ReleaseStepService } from './release-step.service';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { IReleaseGroup } from 'app/shared/model/release-group.model';
import { ReleaseGroupService } from 'app/entities/release-group/release-group.service';

type SelectableEntity = IMicroservice | IReleaseGroup;

@Component({
  selector: 'jhi-release-step-update',
  templateUrl: './release-step-update.component.html',
})
export class ReleaseStepUpdateComponent implements OnInit {
  isSaving = false;
  microservices: IMicroservice[] = [];
  releasegroups: IReleaseGroup[] = [];

  editForm = this.fb.group({
    id: [],
    workItem: [null, Validators.required],
    releaseGroup: [],
  });

  constructor(
    protected releaseStepService: ReleaseStepService,
    protected microserviceService: MicroserviceService,
    protected releaseGroupService: ReleaseGroupService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releaseStep }) => {
      this.updateForm(releaseStep);

      this.microserviceService.query().subscribe((res: HttpResponse<IMicroservice[]>) => (this.microservices = res.body || []));

      this.releaseGroupService.query().subscribe((res: HttpResponse<IReleaseGroup[]>) => (this.releasegroups = res.body || []));
    });
  }

  updateForm(releaseStep: IReleaseStep): void {
    this.editForm.patchValue({
      id: releaseStep.id,
      workItem: releaseStep.workItem,
      releaseGroup: releaseStep.releaseGroup,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releaseStep = this.createFromForm();
    if (releaseStep.id !== undefined) {
      this.subscribeToSaveResponse(this.releaseStepService.update(releaseStep));
    } else {
      this.subscribeToSaveResponse(this.releaseStepService.create(releaseStep));
    }
  }

  private createFromForm(): IReleaseStep {
    return {
      ...new ReleaseStep(),
      id: this.editForm.get(['id'])!.value,
      workItem: this.editForm.get(['workItem'])!.value,
      releaseGroup: this.editForm.get(['releaseGroup'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleaseStep>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
