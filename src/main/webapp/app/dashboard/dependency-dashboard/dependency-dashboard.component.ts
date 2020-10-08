import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { DataSet } from 'vis-data/peer';
import { Network } from 'vis-network/peer';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { IDependency } from '../../shared/model/dependency.model';
import { map } from 'rxjs/operators';
import { IMicroservice } from '../../shared/model/microservice.model';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements AfterViewInit {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;

  networkInstance: any;
  searchValue?: IMicroservice;
  onlyIncomingFilter = true;
  onlyOutgoingFilter = true;

  constructor(protected dependencyService: DependencyService) {}

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

    this.loadAll();
  }

  onFilterChange(): any {
    // eslint-disable-next-line no-console
    console.log(this.onlyOutgoingFilter + ' ' + this.onlyIncomingFilter);
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
}
