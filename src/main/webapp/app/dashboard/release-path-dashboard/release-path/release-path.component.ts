import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { IReleasePath } from 'app/shared/model/release-path.model';
import { ActivatedRoute } from '@angular/router';
import { VisNetworkService } from 'app/shared/vis/vis-network.service';
import { NodeColorsService } from '../node-colors.service';
import { DataSet } from 'vis-data/peer';
import { IReleaseGroup } from 'app/shared/model/release-group.model';

@Component({
  selector: 'jhi-release-path',
  templateUrl: './release-path.component.html',
})
export class ReleasePathComponent implements OnInit, AfterViewInit {
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

    const options = {
      height: '100%',
      width: '100%',
      nodes: {
        shape: 'box',
        font: {
          color: 'white',
        },
      },
      clickToUse: false,
      edges: {
        smooth: false,
        arrows: {
          to: {
            enabled: true,
            type: 'vee',
          },
        },
      },
      interaction: {
        multiselect: true,
      },
      layout: {
        hierarchical: {},
      },
    };

    this.networkInstance = this.visNetworkService.createNetwork(container, options);

    const nodes = new DataSet<any>();
    const edges = new DataSet<any>();
    const targetId = this.releasePath?.target?.id;

    if (this.releasePath && this.releasePath.groups) {
      let groupIndex = 0;
      const groups = this.releasePath.groups;
      const groupsCount = groups.length;
      this.releasePath.groups?.forEach(g => {
        let nodeColor = this.nodeColorsService.getColor(groupIndex);
        if (this.containsTargetMicroservice(g, targetId)) {
          nodeColor = this.nodeColorsService.getActiveColor();
        }

        nodes.add({
          id: groupIndex,
          label: this.generateLabel(g, groupIndex),
          color: nodeColor,
        });

        if (groupIndex < groupsCount - 1) {
          edges.add({
            from: groupIndex,
            to: groupIndex + 1,
          });
        }

        ++groupIndex;
      });
    }

    this.networkInstance.setData({ nodes, edges });
  }

  /**
   * Checks if given IReleaseGroup contains target microservice, for which release path is built
   * @param group with workItems
   * @param targetId of target microservice, for which release path is built
   */
  private containsTargetMicroservice(group: IReleaseGroup, targetId?: number): boolean {
    const step = group.steps?.filter(s => s.workItem?.id === targetId).pop();
    if (step) {
      return true;
    } else {
      return false;
    }
  }

  private generateLabel(group: IReleaseGroup, groupIndex: number): string {
    let label = groupIndex + 1 + '. ';

    group.steps?.forEach(s => {
      label = label + '[' + s.workItem?.name + '], ';
    });

    label = label.substr(0, label.length - 2);

    return label;
  }
}
