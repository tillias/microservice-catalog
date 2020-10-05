import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import 'vis-network/styles/vis-network.css';
import { DataSet } from 'vis-data/peer';
import { Network } from 'vis-network/peer';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit, AfterViewInit {
  @ViewChild('visNetwork', { static: false }) visNetwork!: ElementRef;
  private networkInstance: any;

  constructor() {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    // create an array with nodes
    const nodes = new DataSet<any>([
      { id: 1, label: 'Node 1' },
      { id: 2, label: 'Node 2' },
      { id: 3, label: 'Node 3' },
      { id: 4, label: 'Node 4' },
      { id: 5, label: 'Node 5' },
    ]);

    // create an array with edges
    const edges = new DataSet<any>([
      { from: '1', to: '3' },
      { from: '1', to: '2' },
      { from: '2', to: '4' },
      { from: '2', to: '5' },
    ]);

    const data = { nodes, edges };

    const container = this.visNetwork;
    this.networkInstance = new Network(container.nativeElement, data, {});
  }
}
