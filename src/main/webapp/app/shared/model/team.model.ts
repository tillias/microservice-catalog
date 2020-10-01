export interface ITeam {
  id?: number;
  name?: string;
  itProductOwner?: string;
  productOwner?: string;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public itProductOwner?: string, public productOwner?: string) {}
}
