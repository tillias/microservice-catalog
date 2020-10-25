import { ElementRef, Injectable } from '@angular/core';
import { Network, Options } from 'vis-network/peer';

@Injectable({
  providedIn: 'root',
})
export class VisNetworkService {
  createNetwork(element: ElementRef, options?: Options): Network {
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
    };

    return new Network(element.nativeElement, {}, options || defaultOptions);
  }
}
