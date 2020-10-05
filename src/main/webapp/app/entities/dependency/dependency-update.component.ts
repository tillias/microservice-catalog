import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IDependency, Dependency } from 'app/shared/model/dependency.model';
import { DependencyService } from './dependency.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';

@Component({
  selector: 'jhi-dependency-update',
  templateUrl: './dependency-update.component.html',
})
export class DependencyUpdateComponent implements OnInit {
  isSaving = false;
  microservices: IMicroservice[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    source: [null, Validators.required],
    target: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected dependencyService: DependencyService,
    protected microserviceService: MicroserviceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependency }) => {
      this.updateForm(dependency);

      this.microserviceService.query().subscribe((res: HttpResponse<IMicroservice[]>) => (this.microservices = res.body || []));
    });
  }

  updateForm(dependency: IDependency): void {
    this.editForm.patchValue({
      id: dependency.id,
      name: dependency.name,
      description: dependency.description,
      source: dependency.source,
      target: dependency.target,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('microcatalogApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dependency = this.createFromForm();
    if (dependency.id !== undefined) {
      this.subscribeToSaveResponse(this.dependencyService.update(dependency));
    } else {
      this.subscribeToSaveResponse(this.dependencyService.create(dependency));
    }
  }

  private createFromForm(): IDependency {
    return {
      ...new Dependency(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      source: this.editForm.get(['source'])!.value,
      target: this.editForm.get(['target'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDependency>>): void {
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
