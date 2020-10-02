import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IMicroservice } from 'app/shared/model/microservice.model';

@Component({
  selector: 'jhi-microservice-detail',
  templateUrl: './microservice-detail.component.html',
})
export class MicroserviceDetailComponent implements OnInit {
  microservice: IMicroservice | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microservice }) => (this.microservice = microservice));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
