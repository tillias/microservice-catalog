import { ITeam } from 'app/shared/model/team.model';

export interface IMicroservice {
  id?: number;
  name?: string;
  description?: any;
  imageUrl?: string;
  swaggerUrl?: string;
  gitUrl?: string;
  team?: ITeam;
}

export class Microservice implements IMicroservice {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public imageUrl?: string,
    public swaggerUrl?: string,
    public gitUrl?: string,
    public team?: ITeam
  ) {}
}
