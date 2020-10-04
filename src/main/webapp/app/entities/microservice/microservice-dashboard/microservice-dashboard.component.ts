import { Component, OnDestroy, OnInit } from '@angular/core';
import { MicroserviceService } from '../microservice.service';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-microservice-dashboard',
  templateUrl: './microservice-dashboard.component.html',
  styleUrls: ['./microservice-dashboard.component.scss'],
})
export class MicroserviceDashboardComponent implements OnInit, OnDestroy {
  microservices?: IMicroservice[];
  eventSubscriber?: Subscription;
  search: any;

  constructor(protected microserviceService: MicroserviceService, protected eventManager: JhiEventManager) {}

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMicroservices();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  loadAll(): void {
    this.microserviceService.query().subscribe((res: HttpResponse<IMicroservice[]>) => (this.microservices = res.body || []));
  }

  registerChangeInMicroservices(): void {
    this.eventSubscriber = this.eventManager.subscribe('microserviceListModification', () => this.loadAll());
  }
}
