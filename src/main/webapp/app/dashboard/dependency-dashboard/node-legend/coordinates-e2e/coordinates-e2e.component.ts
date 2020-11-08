import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from 'app/shared/vis/events/VisEvents';

@Component({
  selector: 'jhi-coordinates-e2e',
  templateUrl: './coordinates-e2e.component.html',
})
export class CoordinatesE2eComponent implements OnInit {
  @Input()
  nodeSelection?: ISelectPayload;

  @Input()
  edgeSelection?: ISelectPayload;

  constructor() {}

  ngOnInit(): void {}
}
