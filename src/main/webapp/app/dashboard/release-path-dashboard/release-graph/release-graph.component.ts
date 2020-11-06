import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VisNetworkService } from 'app/shared/vis/vis-network.service';
import { NodeColorsService } from 'app/dashboard/release-path-dashboard/node-colors.service';
import { IReleasePath } from 'app/shared/model/release-path.model';
import { DataSet } from 'vis-data/peer';

@Component({
  selector: 'jhi-release-graph',
  templateUrl: './release-graph.component.html',
})
export class ReleaseGraphComponent implements OnInit, AfterViewInit {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;
  networkInstance: any;

  releasePath?: IReleasePath;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected visNetworkService: VisNetworkService,
    protected nodeColorsService: NodeColorsService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releasePath }) => {
      this.releasePath = releasePath;
    });
  }

  ngAfterViewInit(): void {
    const container = this.visNetwork;

    this.networkInstance = this.visNetworkService.createNetwork(container);

    const nodes = new DataSet<any>();
    const edges = new DataSet<any>();
    const targetId = this.releasePath?.target?.id;

    if (this.releasePath) {
      let groupIndex = 0;
      this.releasePath.groups?.forEach(g => {
        g.steps?.forEach(s => {
          const workItemId = s.workItem?.id;

          let nodeColor = this.nodeColorsService.getColor(groupIndex);
          if (workItemId === targetId) {
            nodeColor = this.nodeColorsService.getActiveColor();
          }

          nodes.add({
            id: workItemId,
            label: s.workItem?.name,
            color: nodeColor,
          });

          s.parentWorkItems?.forEach(pw => {
            edges.add({
              from: workItemId,
              to: pw.id,
            });
          });
        });

        ++groupIndex;
      });
    }

    this.networkInstance.setData({ nodes, edges });
  }
}
