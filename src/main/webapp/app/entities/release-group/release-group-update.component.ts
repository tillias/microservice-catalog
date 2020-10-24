import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IReleaseGroup, ReleaseGroup } from 'app/shared/model/release-group.model';
import { ReleaseGroupService } from './release-group.service';
import { IReleasePath } from 'app/shared/model/release-path.model';
import { ReleasePathService } from 'app/entities/release-path/release-path.service';

@Component({
  selector: 'jhi-release-group-update',
  templateUrl: './release-group-update.component.html',
})
export class ReleaseGroupUpdateComponent implements OnInit {
  isSaving = false;
  releasepaths: IReleasePath[] = [];

  editForm = this.fb.group({
    id: [],
    order: [null, [Validators.required, Validators.min(0)]],
    releasePath: [],
  });

  constructor(
    protected releaseGroupService: ReleaseGroupService,
    protected releasePathService: ReleasePathService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releaseGroup }) => {
      this.updateForm(releaseGroup);

      this.releasePathService.query().subscribe((res: HttpResponse<IReleasePath[]>) => (this.releasepaths = res.body || []));
    });
  }

  updateForm(releaseGroup: IReleaseGroup): void {
    this.editForm.patchValue({
      id: releaseGroup.id,
      order: releaseGroup.order,
      releasePath: releaseGroup.releasePath,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const releaseGroup = this.createFromForm();
    if (releaseGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.releaseGroupService.update(releaseGroup));
    } else {
      this.subscribeToSaveResponse(this.releaseGroupService.create(releaseGroup));
    }
  }

  private createFromForm(): IReleaseGroup {
    return {
      ...new ReleaseGroup(),
      id: this.editForm.get(['id'])!.value,
      order: this.editForm.get(['order'])!.value,
      releasePath: this.editForm.get(['releasePath'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReleaseGroup>>): void {
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

  trackById(index: number, item: IReleasePath): any {
    return item.id;
  }
}
