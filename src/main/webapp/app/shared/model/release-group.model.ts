import { IReleaseStep } from 'app/shared/model/release-step.model';
import { IReleasePath } from 'app/shared/model/release-path.model';

export interface IReleaseGroup {
  id?: number;
  steps?: IReleaseStep[];
  releasePath?: IReleasePath;
}

export class ReleaseGroup implements IReleaseGroup {
  constructor(public id?: number, public steps?: IReleaseStep[], public releasePath?: IReleasePath) {}
}
