import { Component, OnInit } from '@angular/core';
import { IMicroservice } from '../../../shared/model/microservice.model';
import { JhiDataUtils } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-microservice-details',
  templateUrl: './microservice-custom-details.component.html',
  styleUrls: ['./microservice-custom-details.component.scss'],
})
export class MicroserviceCustomDetailsComponent implements OnInit {
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
