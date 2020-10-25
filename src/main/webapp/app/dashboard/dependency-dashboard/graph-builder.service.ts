import { Injectable } from '@angular/core';
import { DataSet } from 'vis-data/peer';
import { IDependency } from '../../shared/model/dependency.model';
import { IMicroservice } from '../../shared/model/microservice.model';
import { Network } from 'vis-network/peer';
import { DependencyService } from '../../entities/dependency/dependency.service';
import { MicroserviceService } from '../../entities/microservice/microservice.service';
import { forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

export class FilterContext {
  constructor(public onlyIncomingFilter: boolean, public onlyOutgoingFilter: boolean, public filter?: IMicroservice) {}
}

class GraphContext {
  constructor(
    public network: Network,
    public microservices: IMicroservice[],
    public dependencies: IDependency[],
    public filterContext: FilterContext
  ) {}
}

@Injectable({
  providedIn: 'root',
})
export class GraphBuilderService {
  constructor(protected dependencyService: DependencyService, protected microserviceService: MicroserviceService) {}

  refreshGraph(network: Network, filterContext: FilterContext): void {
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
        this.build(new GraphContext(network, results.microservices, results.dependencies, filterContext));
      });
  }

  private build(context: GraphContext): void {
    let filteredDependencies = context.dependencies;

    const filterContext = context.filterContext;

    if (filterContext.filter) {
      const searchID = filterContext.filter.id;
      filteredDependencies = context.dependencies.filter(d => {
        if (filterContext.onlyIncomingFilter && !filterContext.onlyOutgoingFilter) {
          return d.target?.id === searchID;
        }

        if (filterContext.onlyOutgoingFilter && !filterContext.onlyIncomingFilter) {
          return d.source?.id === searchID;
        }

        if (filterContext.onlyIncomingFilter && filterContext.onlyOutgoingFilter) {
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
          id: d.id,
          from: sourceID,
          to: targetID,
        });

        nodeIds.add(sourceID);
        nodeIds.add(targetID);
      }
    });

    let filteredMicroservices = context.microservices;
    if (filterContext.filter) {
      filteredMicroservices = context.microservices.filter(m => nodeIds.has(m.id));
    }

    const nodes = new DataSet<any>(filteredMicroservices.map(m => this.convertToGraphNode(m)));

    context.network.setData({ nodes, edges });
  }

  protected convertToGraphNode(microservice: IMicroservice): any {
    return {
      id: microservice.id,
      label: microservice.name,
    };
  }
}
