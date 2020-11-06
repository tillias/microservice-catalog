import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IResult } from 'app/shared/model/impact/analysis/result.model';
import { VisNetworkService } from 'app/shared/vis/vis-network.service';
import { DataSet } from 'vis-data/peer';
import { NodeColorsService } from 'app/dashboard/release-path-dashboard/node-colors.service';

@Component({
  selector: 'jhi-impact-analysis-graph',
  templateUrl: './impact-analysis-graph.component.html',
})
export class ImpactAnalysisGraphComponent implements OnInit, AfterViewInit {
  @ViewChild('visNetwork', { static: false })
  visNetwork!: ElementRef;

  networkInstance: any;

  analysisResult?: IResult;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected visNetworkService: VisNetworkService,
    protected nodeColorsService: NodeColorsService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analysisResult }) => {
      this.analysisResult = analysisResult;
    });
  }

  ngAfterViewInit(): void {
    const container = this.visNetwork;

    this.networkInstance = this.visNetworkService.createNetwork(container);

    const nodes = new DataSet<any>();
    const edges = new DataSet<any>();
    const analysisTargetId = this.analysisResult?.target?.id;

    if (this.analysisResult) {
      let groupIndex = 0;
      this.analysisResult.groups?.forEach(g => {
        g.items?.forEach(i => {
          const itemTargetId = i.target?.id;
          let nodeColor = this.nodeColorsService.getColor(groupIndex);
          if (analysisTargetId === itemTargetId) {
            nodeColor = this.nodeColorsService.getActiveColor();
          } else {
            ++groupIndex;
          }

          nodes.add({
            id: itemTargetId,
            label: i.target?.name,
            color: nodeColor,
          });

          i.siblings?.forEach(s => {
            edges.add({
              from: itemTargetId,
              to: s.id,
            });
          });
        });
      });
    }

    this.networkInstance.setData({ nodes, edges });
  }
}
