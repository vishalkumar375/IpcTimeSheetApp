export interface ITaskType {
    id?: number;
    taskType?: string;
}

export class TaskType implements ITaskType {
    constructor(public id?: number, public taskType?: string) {}
}
