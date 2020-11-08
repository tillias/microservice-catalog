import { ElementRef, Injectable } from '@angular/core';
import { Network, Options } from 'vis-network/peer';
import { GRAPH_FIXED_SEED } from 'app/app.constants';

@Injectable({
  providedIn: 'root',
})
export class VisNetworkService {
  fixedSeed = GRAPH_FIXED_SEED;
  seed?: number;

  constructor() {
    this.seed = undefined; // use random by default
  }

  createNetwork(element: ElementRef, options?: Options): Network {
    if (this.fixedSeed) {
      this.seed = 7;
    }

    const defaultOptions = {
      height: '100%',
      width: '100%',
      nodes: {
        shape: 'hexagon',
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
        randomSeed: this.seed,
      },
    };

    return new Network(element.nativeElement, {}, options || defaultOptions);
  }
}
