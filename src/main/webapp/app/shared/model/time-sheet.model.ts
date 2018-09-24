import { Moment } from 'moment';
import { ITaskType } from 'app/shared/model/task-type.model';
import { IUser } from 'app/core';
import { IProjectCode } from 'app/shared/model/project-code.model';

export interface ITimeSheet {
    id?: number;
    forDate?: Moment;
    actualHours?: number;
    comments?: string;
    taskType?: ITaskType;
    projectCode?: IProjectCode;
    user?: IUser;
    copyToDates?:Moment[];
    
}

export class TimeSheet implements ITimeSheet {
    constructor(
        public id?: number,
        public forDate?: Moment,
        public actualHours?: number,
        public comments?: string,
        public taskType?: ITaskType,
        public projectCode?: IProjectCode,
        public user?: IUser,
        public copyToDates?:Moment[],
       
    ) {}
}
