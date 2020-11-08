export interface IPointer {
  DOM: ICoordinates;
  canvas: ICoordinates;
}

class Pointer implements IPointer {
  constructor(public DOM: ICoordinates, public canvas: ICoordinates) {}
}

export interface ICoordinates {
  x: number;
  y: number;
}

class Coordinates implements ICoordinates {
  constructor(public x: number, public y: number) {}
}

export interface ISelectPayload {
  pointer: IPointer;
  nodes: number[];
  edges: number[];

  hasEdges(): boolean;

  hasNodes(): boolean;

  firstNode(): number;

  firstEdge(): number;
}

export class SelectPayload implements ISelectPayload {
  public pointer: IPointer;
  public nodes: number[];
  public edges: number[];

  constructor(json: any) {
    this.pointer = new Pointer(
      new Coordinates(json.pointer.DOM.x, json.pointer.DOM.y),
      new Coordinates(json.pointer.canvas.x, json.pointer.canvas.y)
    );

    this.nodes = json.nodes;
    this.edges = json.edges;
  }

  hasEdges(): boolean {
    return this.edges && this.edges.length > 0;
  }

  hasNodes(): boolean {
    return this.nodes && this.nodes.length > 0;
  }

  firstNode(): number {
    return this.nodes[0];
  }

  firstEdge(): number {
    return this.edges[0];
  }
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
