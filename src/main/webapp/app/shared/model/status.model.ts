export interface IStatus {
  id?: number;
  name?: string;
  description?: string;
}

export class Status implements IStatus {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
