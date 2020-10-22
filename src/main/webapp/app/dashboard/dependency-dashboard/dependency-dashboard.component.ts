import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DataSet } from 'vis-data/peer';
import { Network } from 'vis-network/peer';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { IDependency } from '../../shared/model/dependency.model';
import { IMicroservice } from '../../shared/model/microservice.model';
import { ISelectPayload } from '../../shared/vis/events/VisEvents';
import { EXPERIMENTAL_FEATURE } from '../../app.constants';
import { CreateDependencyDialogService } from './create-dependency-dialog/create-dependency-dialog.service';
import { JhiEventManager } from 'ng-jhipster';
import { forkJoin, Subscription } from 'rxjs';
import { MicroserviceService } from '../../entities/microservice/microservice.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;

  subscription?: Subscription;

  networkInstance: any;
  searchValue?: IMicroservice;
  onlyIncomingFilter = true;
  onlyOutgoingFilter = true;
  selection?: ISelectPayload;
  experimentalFeatures = EXPERIMENTAL_FEATURE;

  constructor(
    protected eventManager: JhiEventManager,
    protected dependencyService: DependencyService,
    protected microserviceService: MicroserviceService,
    protected createDependencyDialogService: CreateDependencyDialogService
  ) {}

  ngOnInit(): void {
    this.registerChangeInDependencies();
  }

  registerChangeInDependencies(): void {
    this.subscription = this.eventManager.subscribe('dependencyListModification', () => this.loadAll());
    this.subscription.add(this.eventManager.subscribe('microserviceListModification', () => this.loadAll()));
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

    if (this.subscription) {
      this.eventManager.destroy(this.subscription);
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
    const dependencies$ = this.dependencyService.query();
    const microservices$ = this.microserviceService.query();

    forkJoin({ dependencies$, microservices$ })
      .pipe(
        map(result => {
          return {
            dependencies: result.dependencies$.body || [],
            microservices: result.microservices$.body || [],
          };
        })
      )
      .subscribe(results => {
        this.refreshGraph(results.dependencies, results.microservices);
      });
  }

  refreshGraph(dependencies: IDependency[], microservices: IMicroservice[]): void {
    let filteredDependencies = dependencies;

    if (this.searchValue) {
      const searchID = this.searchValue.id;
      filteredDependencies = dependencies.filter(d => {
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

    const edges = new DataSet<any>();
    const nodeIds = new Set();

    filteredDependencies.forEach(d => {
      if (d.source != null && d.target != null) {
        const sourceID = d.source.id;
        const targetID = d.target.id;

        edges.add({
          from: sourceID,
          to: targetID,
        });

        nodeIds.add(sourceID);
        nodeIds.add(targetID);
      }
    });

    let filteredMicroservices = microservices;
    if (this.searchValue) {
      filteredMicroservices = microservices.filter(m => nodeIds.has(m.id));
    }

    const nodes = new DataSet<any>(filteredMicroservices.map(m => this.convertToGraphNode(m)));

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
    // Use selected microservice as dependency's start
    if (this.selection) {
      const id = this.selection.nodes[0];
      this.microserviceService
        .find(id)
        .pipe(map(r => r.body || undefined))
        .subscribe(r => this.openDialog(r));
    } else {
      this.openDialog(this.searchValue);
    }
  }

  openDialog(initialSource?: IMicroservice): void {
    this.createDependencyDialogService.open(initialSource);
  }
}
