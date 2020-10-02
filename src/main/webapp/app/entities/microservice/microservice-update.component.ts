import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IMicroservice, Microservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from './microservice.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from 'app/entities/team/team.service';

@Component({
  selector: 'jhi-microservice-update',
  templateUrl: './microservice-update.component.html',
})
export class MicroserviceUpdateComponent implements OnInit {
  isSaving = false;
  teams: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    imageUrl: [null, [Validators.required]],
    swaggerUrl: [null, [Validators.required]],
    gitUrl: [null, [Validators.required]],
    team: [null, Validators.required],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected microserviceService: MicroserviceService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microservice }) => {
      this.updateForm(microservice);

      this.teamService.query().subscribe((res: HttpResponse<ITeam[]>) => (this.teams = res.body || []));
    });
  }

  updateForm(microservice: IMicroservice): void {
    this.editForm.patchValue({
      id: microservice.id,
      name: microservice.name,
      description: microservice.description,
      imageUrl: microservice.imageUrl,
      swaggerUrl: microservice.swaggerUrl,
      gitUrl: microservice.gitUrl,
      team: microservice.team,
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
    const microservice = this.createFromForm();
    if (microservice.id !== undefined) {
      this.subscribeToSaveResponse(this.microserviceService.update(microservice));
    } else {
      this.subscribeToSaveResponse(this.microserviceService.create(microservice));
    }
  }

  private createFromForm(): IMicroservice {
    return {
      ...new Microservice(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      imageUrl: this.editForm.get(['imageUrl'])!.value,
      swaggerUrl: this.editForm.get(['swaggerUrl'])!.value,
      gitUrl: this.editForm.get(['gitUrl'])!.value,
      team: this.editForm.get(['team'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMicroservice>>): void {
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

  trackById(index: number, item: ITeam): any {
    return item.id;
  }
}
