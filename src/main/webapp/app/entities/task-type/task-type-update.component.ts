import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ITaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from './task-type.service';

@Component({
    selector: 'jhi-task-type-update',
    templateUrl: './task-type-update.component.html'
})
export class TaskTypeUpdateComponent implements OnInit {
    private _taskType: ITaskType;
    isSaving: boolean;

    constructor(private taskTypeService: TaskTypeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ taskType }) => {
            this.taskType = taskType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.taskType.id !== undefined) {
            this.subscribeToSaveResponse(this.taskTypeService.update(this.taskType));
        } else {
            this.subscribeToSaveResponse(this.taskTypeService.create(this.taskType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITaskType>>) {
        result.subscribe((res: HttpResponse<ITaskType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get taskType() {
        return this._taskType;
    }

    set taskType(taskType: ITaskType) {
        this._taskType = taskType;
    }
}
