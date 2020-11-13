import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from 'app/shared/vis/events/VisEvents';
import { DEVELOPMENT_COMPONENTS, GRAPH_FIXED_SEED } from 'app/app.constants';

@Component({
  selector: 'jhi-node-legend',
  templateUrl: './graph-legend.component.html',
})
export class GraphLegendComponent implements OnInit {
  @Input() nodeSelection?: ISelectPayload;
  @Input() edgeSelection?: ISelectPayload;

  graphFixedSeed = GRAPH_FIXED_SEED;
  developmentComponents = DEVELOPMENT_COMPONENTS;

  constructor() {}

  ngOnInit(): void {}
}
