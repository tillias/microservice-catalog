import { Moment } from 'moment';
import { IMicroservice } from '../../microservice.model';
import { IGroup } from './group.model';

export interface IResult {
  createdOn?: Moment;
  target?: IMicroservice;
  groups?: IGroup[];
}

export class Result implements IResult {
  constructor(public createdOn?: Moment, public groups?: IGroup[], public target?: IMicroservice) {}
}
