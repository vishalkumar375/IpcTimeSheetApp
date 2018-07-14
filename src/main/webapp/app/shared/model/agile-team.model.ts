export interface IAgileTeam {
    id?: number;
    teamName?: string;
}

export class AgileTeam implements IAgileTeam {
    constructor(public id?: number, public teamName?: string) {}
}
