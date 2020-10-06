import { IMicroservice } from 'app/shared/model/microservice.model';

export interface IDependency {
  id?: number;
  name?: string;
  description?: any;
  source?: IMicroservice;
  target?: IMicroservice;
}

export class Dependency implements IDependency {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public source?: IMicroservice,
    public target?: IMicroservice
  ) {}
}
