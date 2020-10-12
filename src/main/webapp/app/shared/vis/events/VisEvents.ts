export interface ISelectPayload {
  pointer: {
    DOM: ICoordinates;
    canvas: ICoordinates;
  };

  nodes: number[];
  edges: string[];
}

export interface IDeselectEvent {
  pointer: {
    DOM: ICoordinates;
    canvas: ICoordinates;
  };

  previousSelection: {
    nodes: string[];
    edges: string[];
  };
}

export interface ICoordinates {
  x: number;
  y: number;
}
