import { IMicroservice } from 'app/shared/model/microservice.model';
import { IReleaseGroup } from 'app/shared/model/release-group.model';

export interface IReleaseStep {
  id?: number;
  workItem?: IMicroservice;
  releaseGroup?: IReleaseGroup;
}

export class ReleaseStep implements IReleaseStep {
  constructor(public id?: number, public workItem?: IMicroservice, public releaseGroup?: IReleaseGroup) {}
}
