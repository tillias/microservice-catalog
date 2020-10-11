import { ITeam } from 'app/shared/model/team.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IMicroserviceGroupFilter {
  caseSensitive: boolean;
  name?: string;
  description?: string;
  swaggerUrl?: string;
  gitUrl?: string;
  team?: ITeam;
  status?: IStatus;
}

export class MicroserviceGroupFilter implements IMicroserviceGroupFilter {
  caseSensitive = false;
}
