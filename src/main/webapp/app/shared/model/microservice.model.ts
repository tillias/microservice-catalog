import { ITeam } from 'app/shared/model/team.model';
import { IStatus } from 'app/shared/model/status.model';

export interface IMicroservice {
  id?: number;
  name?: string;
  description?: any;
  imageUrl?: string;
  swaggerUrl?: string;
  gitUrl?: string;
  ciUrl?: string;
  team?: ITeam;
  status?: IStatus;
}

export class Microservice implements IMicroservice {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public imageUrl?: string,
    public swaggerUrl?: string,
    public gitUrl?: string,
    public ciUrl?: string,
    public team?: ITeam,
    public status?: IStatus
  ) {}
}
