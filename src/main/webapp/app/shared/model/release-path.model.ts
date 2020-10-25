import { Moment } from 'moment';
import { IReleaseGroup } from 'app/shared/model/release-group.model';
import { IMicroservice } from 'app/shared/model/microservice.model';

export interface IReleasePath {
  createdOn?: Moment;
  groups?: IReleaseGroup[];
  target?: IMicroservice;
}

export class ReleasePath implements IReleasePath {
  constructor(public createdOn?: Moment, public groups?: IReleaseGroup[], public target?: IMicroservice) {}
}
