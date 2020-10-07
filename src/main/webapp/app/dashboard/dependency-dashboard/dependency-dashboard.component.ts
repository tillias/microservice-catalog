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

  loadAll(): void {
    this.dependencyService
      .query()
      .pipe(map(httpResponse => httpResponse.body))
      .subscribe(dependencies => this.refreshGraph(dependencies || []));
  }

  refreshGraph(dependencies: IDependency[]): void {
    const edges = new DataSet<any>();

    const microservicesMap = new Map<number, IMicroservice>();

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
}
