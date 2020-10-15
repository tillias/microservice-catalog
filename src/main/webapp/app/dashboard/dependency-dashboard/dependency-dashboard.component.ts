import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSet } from 'vis-data/peer';
import { Network } from 'vis-network/peer';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { IDependency } from '../../shared/model/dependency.model';
import { map } from 'rxjs/operators';
import { IMicroservice } from '../../shared/model/microservice.model';
import { ISelectPayload } from '../../shared/vis/events/VisEvents';
import { EXPERIMENTAL_FEATURE } from '../../app.constants';
import { CreateDependencyDialogService } from './create-dependency-dialog/create-dependency-dialog.service';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;

  dependenciesSubscriber?: Subscription;
  microservicesSubscriber?: Subscription;

  networkInstance: any;
  searchValue?: IMicroservice;
  onlyIncomingFilter = true;
  onlyOutgoingFilter = true;
  selection?: ISelectPayload;
  experimentalFeatures = EXPERIMENTAL_FEATURE;

  constructor(
    protected eventManager: JhiEventManager,
    protected dependencyService: DependencyService,
    protected createDependencyDialogService: CreateDependencyDialogService
  ) {}

  ngOnInit(): void {
    this.registerChangeInDependencies();
  }

  registerChangeInDependencies(): void {
    this.dependenciesSubscriber = this.eventManager.subscribe('dependencyListModification', () => this.loadAll());
    this.microservicesSubscriber = this.eventManager.subscribe('microserviceListModification', () => this.loadAll());
  }

  ngAfterViewInit(): void {
    const container = this.visNetwork;

    const data = {};
    this.networkInstance = new Network(container.nativeElement, data, {
      height: '100%',
      width: '100%',
      nodes: {
        shape: 'hexagon',
        font: {
          color: 'white',
        },
      },
      clickToUse: false,
      edges: {
        smooth: false,
        arrows: {
          to: {
            enabled: true,
            type: 'vee',
          },
        },
      },
    });

    // See Network.d.ts -> NetworkEvents
    this.networkInstance.on('selectNode', (params: any) => {
      this.handleSelectNode(params);
    });
    this.networkInstance.on('deselectNode', () => {
      this.handleDeselectNode();
    });

    this.loadAll();
  }

  ngOnDestroy(): void {
    this.networkInstance.off('selectNode');
    this.networkInstance.off('deselectNode');

    if (this.dependenciesSubscriber) {
      this.eventManager.destroy(this.dependenciesSubscriber);
    }
    if (this.microservicesSubscriber) {
      this.eventManager.destroy(this.microservicesSubscriber);
    }
  }

  handleSelectNode(payload: ISelectPayload): void {
    this.selection = payload;
  }

  handleDeselectNode(): void {
    this.selection = undefined;
  }

  onFilterChange(): any {
    // Only makes sense to refresh if filter for particular microservice is active
    if (this.searchValue) {
      this.loadAll();
    }
  }

  loadAll(): void {
    this.dependencyService
      .query()
      .pipe(map(httpResponse => httpResponse.body))
      .subscribe(dependencies => this.refreshGraph(dependencies || []));
  }

  refreshGraph(dependencies: IDependency[]): void {
    const edges = new DataSet<any>();

    const microservicesMap = new Map<number, IMicroservice>();

    if (this.searchValue) {
      const searchID = this.searchValue.id;
      dependencies = dependencies.filter(d => {
        if (this.onlyIncomingFilter && !this.onlyOutgoingFilter) {
          return d.target?.id === searchID;
        }

        if (this.onlyOutgoingFilter && !this.onlyIncomingFilter) {
          return d.source?.id === searchID;
        }

        if (this.onlyIncomingFilter && this.onlyOutgoingFilter) {
          return d.source?.id === searchID || d.target?.id === searchID;
        }

        return false;
      });
    }

    dependencies.forEach(d => {
      if (d.source != null && d.target != null) {
        microservicesMap.set(d.source.id!, d.source);
        microservicesMap.set(d.target.id!, d.target);

        edges.add({
          from: d.source.id,
          to: d.target.id,
        });
      }
    });

    const nodes = new DataSet<any>([...microservicesMap.values()].map(m => this.convertToGraphNode(m)));

    const data = { nodes, edges };

    this.networkInstance.setData(data);
  }

  convertToGraphNode(microservice: IMicroservice): any {
    return {
      id: microservice.id,
      label: microservice.name,
    };
  }

  onMicroserviceSelected(microservice?: IMicroservice): any {
    this.searchValue = microservice;
    this.loadAll();
  }

  buildDeploymentPath(): void {}

  createDependency(): void {
    this.createDependencyDialogService.open();
  }
}
