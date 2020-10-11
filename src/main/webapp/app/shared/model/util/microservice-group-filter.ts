import { ITeam } from 'app/shared/model/team.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IMicroserviceGroupFilter {
  name?: String;
  description?: String;
  swaggerUrl?: String;
  gitUrl?: String;
  team?: ITeam;
  status?: IStatus;
}

export class MicroserviceGroupFilter implements IMicroserviceGroupFilter {}
