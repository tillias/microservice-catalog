import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { IMicroservice } from '../../shared/model/microservice.model';
import { EXPERIMENTAL_FEATURE } from '../../app.constants';
import { CreateDependencyDialogService } from './create-dependency-dialog/create-dependency-dialog.service';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { MicroserviceService } from '../../entities/microservice/microservice.service';
import { map } from 'rxjs/operators';
import { ISelectPayload, SelectPayload } from '../../shared/vis/events/VisEvents';
import { DeleteDialogService } from './delete-dialog.service';
import { FilterContext, GraphBuilderService } from './graph-builder.service';
import { ReleasePathCustomService } from '../../entities/release-path/custom/release-path-custom.service';
import { VisNetworkService } from '../../shared/vis/vis-network.service';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;
  networkInstance: any;

  subscription?: Subscription;

  searchValue?: IMicroservice;
  onlyIncomingFilter = true;
  onlyOutgoingFilter = true;
  nodeSelection?: ISelectPayload;
  edgeSelection?: ISelectPayload;
  experimentalFeatures = EXPERIMENTAL_FEATURE;

  constructor(
    protected eventManager: JhiEventManager,
    protected dependencyService: DependencyService,
    protected microserviceService: MicroserviceService,
    protected releasePathService: ReleasePathCustomService,
    protected visNetworkService: VisNetworkService,
    protected graphBuilderService: GraphBuilderService,
    protected createDependencyDialogService: CreateDependencyDialogService,
    protected deleteDialogService: DeleteDialogService
  ) {}

  ngOnInit(): void {
    this.registerChangeInDependencies();
  }

  registerChangeInDependencies(): void {
    this.subscription = this.eventManager.subscribe('dependencyListModification', () => this.refreshGraph());
    this.subscription.add(this.eventManager.subscribe('microserviceListModification', () => this.refreshGraph()));
  }

  ngAfterViewInit(): void {
    const container = this.visNetwork;

    this.networkInstance = this.visNetworkService.createNetwork(container);

    // See Network.d.ts -> NetworkEvents
    this.networkInstance.on('selectNode', (params: any) => {
      this.handleSelectNode(params);
    });
    this.networkInstance.on('deselectNode', () => {
      this.handleDeselectNode();
    });
    this.networkInstance.on('selectEdge', (params: any) => {
      this.handleSelectEdge(params);
    });
    this.networkInstance.on('deselectEdge', () => {
      this.handleDeselectEdge();
    });

    this.refreshGraph();
  }

  ngOnDestroy(): void {
    this.networkInstance.off('selectNode');
    this.networkInstance.off('deselectNode');
    this.networkInstance.off('selectEdge');
    this.networkInstance.off('deselectEdge');

    if (this.subscription) {
      this.eventManager.destroy(this.subscription);
    }
  }

  handleSelectNode(payload: any): void {
    this.nodeSelection = new SelectPayload(payload);
  }

  handleSelectEdge(payload: any): void {
    this.edgeSelection = new SelectPayload(payload);
  }

  handleDeselectNode(): void {
    this.nodeSelection = undefined;
  }

  handleDeselectEdge(): void {
    this.edgeSelection = undefined;
  }

  onFilterChange(): any {
    // Only makes sense to refresh if filter for particular microservice is active
    if (this.searchValue) {
      this.refreshGraph();
    }
  }

  refreshGraph(): void {
    const filterContext = new FilterContext(this.onlyIncomingFilter, this.onlyOutgoingFilter, this.searchValue);
    this.graphBuilderService.refreshGraph(this.networkInstance, filterContext);
  }

  onMicroserviceSelected(microservice?: IMicroservice): any {
    this.searchValue = microservice;
    this.refreshGraph();
  }

  selectedNodeId(): number {
    if (this.nodeSelection && this.nodeSelection.hasNodes()) {
      return this.nodeSelection.firstNode();
    }

    return -1;
  }

  selectedEdgeId(): number {
    if (this.edgeSelection && this.edgeSelection.hasEdges()) {
      return this.edgeSelection.firstEdge();
    }

    return -1;
  }

  createDependency(): void {
    // Use selected microservice as dependency's start
    if (this.nodeSelection && this.nodeSelection.hasNodes()) {
      const id = this.nodeSelection.firstNode();
      this.microserviceService
        .find(id)
        .pipe(map(r => r.body || undefined))
        .subscribe(r => this.openCreateDependencyDialog(r));
    } else {
      this.openCreateDependencyDialog(this.searchValue);
    }
  }

  openCreateDependencyDialog(initialSource?: IMicroservice): void {
    this.createDependencyDialogService.open(initialSource);
  }

  deleteDependency(): void {
    if (this.edgeSelection && this.edgeSelection.hasEdges()) {
      const id = this.edgeSelection.firstEdge();
      this.dependencyService
        .find(id)
        .pipe(map(r => r.body || undefined))
        .subscribe(d => {
          this.deleteDialogService.openForDependency(d);
        });
    }
  }

  deleteMicroservice(): void {
    if (this.nodeSelection && this.nodeSelection.hasNodes()) {
      const id = this.nodeSelection.firstNode();
      this.microserviceService
        .find(id)
        .pipe(map(r => r.body || undefined))
        .subscribe(m => {
          this.deleteDialogService.openForMicroservice(m);
        });
    }
  }

  onDeleteKey(): void {
    if (this.selectedNodeId() > 0) {
      this.deleteMicroservice();
    } else if (this.selectedEdgeId() > 0) {
      this.deleteDependency();
    }
  }
}
