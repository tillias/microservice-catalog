import { IReleaseStep } from 'app/shared/model/release-step.model';

export interface IReleaseGroup {
  steps?: IReleaseStep[];
}

export class ReleaseGroup implements IReleaseGroup {
  constructor(public steps?: IReleaseStep[]) {}
}
