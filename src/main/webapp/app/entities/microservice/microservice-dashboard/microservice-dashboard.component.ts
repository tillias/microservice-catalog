import { Component, OnDestroy, OnInit } from '@angular/core';
import { MicroserviceService } from '../microservice.service';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { IMicroserviceGroupFilter, MicroserviceGroupFilter } from 'app/shared/model/util/microservice-group-filter';

@Component({
  selector: 'jhi-microservice-dashboard',
  templateUrl: './microservice-dashboard.component.html',
})
export class MicroserviceDashboardComponent implements OnInit, OnDestroy {
  microservices: IMicroservice[];
  filter: IMicroserviceGroupFilter;
  eventSubscriber?: Subscription;

  constructor(protected microserviceService: MicroserviceService, protected eventManager: JhiEventManager) {
    this.microservices = [];
    this.filter = new MicroserviceGroupFilter();
  }

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

  onMicroserviceSelected(value?: IMicroservice): any {
    if (value) {
      this.microservices = this.microservices?.filter(i => i.id === value.id);
    } else {
      this.loadAll();
    }
  }

  onGroupFilterChanged(groupFilter: IMicroserviceGroupFilter): void {
    this.filter = groupFilter;
    this.loadAll();
  }
}
