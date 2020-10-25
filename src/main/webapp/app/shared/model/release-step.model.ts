import { IMicroservice } from 'app/shared/model/microservice.model';

export interface IReleaseStep {
  workItem?: IMicroservice;
}

export class ReleaseStep implements IReleaseStep {
  constructor(public workItem?: IMicroservice) {}
}
