import { IMicroservice } from '../../microservice.model';

export interface IItem {
  target: IMicroservice;
  siblings: IMicroservice[];
}
