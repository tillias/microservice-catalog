import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { DataSet } from 'vis-data/peer';
import { Network } from 'vis-network/peer';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { IDependency } from '../../shared/model/dependency.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;

  networkInstance: any;

  constructor(protected dependencyService: DependencyService) {}

  ngOnInit(): void {}

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
    const nodes = new DataSet<any>();
    const edges = new DataSet<any>();

    dependencies.forEach(d => {
      nodes.add({
        id: d.id,
        label: d.name,
      });

      edges.add({
        from: d.source?.id,
        to: d.target?.id,
      });
    });

    const data = { nodes, edges };

    this.networkInstance.setData(data);
  }
}
