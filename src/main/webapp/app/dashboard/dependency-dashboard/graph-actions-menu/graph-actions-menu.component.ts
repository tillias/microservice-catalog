import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from '../../../shared/vis/events/VisEvents';

@Component({
  selector: 'jhi-node-actions-menu',
  templateUrl: './graph-actions-menu.component.html',
  styleUrls: ['./graph-actions-menu.component.scss'],
})
export class GraphActionsMenuComponent implements OnInit {
  @Input() selection?: ISelectPayload;

  constructor() {}

  ngOnInit(): void {}
}
