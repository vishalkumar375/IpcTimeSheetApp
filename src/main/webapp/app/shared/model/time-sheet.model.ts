import { Moment } from 'moment';
import { ITaskType } from 'app/shared/model/task-type.model';
import { IUser } from 'app/core';

export interface ITimeSheet {
    id?: number;
    forDate?: Moment;
    actualHours?: number;
    comments?: string;
    taskType?: ITaskType;
    user?: IUser;
    
}

export class TimeSheet implements ITimeSheet {
    constructor(
        public id?: number,
        public forDate?: Moment,
        public actualHours?: number,
        public comments?: string,
        public taskType?: ITaskType,
        public user?: IUser,
       
    ) {}
}
