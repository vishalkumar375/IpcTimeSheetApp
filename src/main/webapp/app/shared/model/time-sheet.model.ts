import { Moment } from 'moment';

export interface ITimeSheet {
    id?: number;
    empID?: number;
    fullName?: string;
    personId?: string;
    forDate?: Moment;
    forDay?: string;
    actualHours?: number;
    comments?: string;
    departmentId?: number;
    agileTeamId?: number;
    projectCodeId?: number;
    taskTypeId?: number;
}

export class TimeSheet implements ITimeSheet {
    constructor(
        public id?: number,
        public empID?: number,
        public fullName?: string,
        public personId?: string,
        public forDate?: Moment,
        public forDay?: string,
        public actualHours?: number,
        public comments?: string,
        public departmentId?: number,
        public agileTeamId?: number,
        public projectCodeId?: number,
        public taskTypeId?: number
    ) {}
}
