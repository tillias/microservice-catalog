import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from 'app/shared/vis/events/VisEvents';

@Component({
  selector: 'jhi-node-legend',
  templateUrl: './graph-legend.component.html',
  styleUrls: ['./graph-legend.component.scss'],
})
export class GraphLegendComponent implements OnInit {
  @Input() nodeSelection?: ISelectPayload;
  @Input() edgeSelection?: ISelectPayload;

  constructor() {}

  ngOnInit(): void {}
}
