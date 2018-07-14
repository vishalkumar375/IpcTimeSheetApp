export interface IProjectCode {
    id?: number;
    projectCode?: string;
}

export class ProjectCode implements IProjectCode {
    constructor(public id?: number, public projectCode?: string) {}
}
