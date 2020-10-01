export interface ITeam {
  id?: number;
  name?: string;
  teamLead?: string;
  productOwner?: string;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public teamLead?: string, public productOwner?: string) {}
}
