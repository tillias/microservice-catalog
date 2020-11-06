import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from 'app/shared/vis/events/VisEvents';
import { DependencyService } from 'app/entities/dependency/dependency.service';
import { map } from 'rxjs/operators';
import { IDependency } from 'app/shared/model/dependency.model';

@Component({
  selector: 'jhi-edge-legend',
  templateUrl: './edge-legend.component.html',
})
export class EdgeLegendComponent implements OnInit {
  selection?: ISelectPayload;
  dependencies?: IDependency[];
  dependency?: IDependency;

  constructor(protected service: DependencyService) {}

  ngOnInit(): void {}

  @Input()
  set edgeSelection(selection: ISelectPayload) {
    this.selection = selection;

    if (selection && selection.hasEdges()) {
      this.service
        .findAllById(selection.edges)
        .pipe(map(response => response.body || undefined))
        .subscribe(d => (this.dependencies = d));
    }
  }
}
