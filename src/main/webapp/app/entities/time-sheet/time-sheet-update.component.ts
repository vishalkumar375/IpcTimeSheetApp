import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { TimeSheetService } from './time-sheet.service';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department';
import { IAgileTeam } from 'app/shared/model/agile-team.model';
import { AgileTeamService } from 'app/entities/agile-team';
import { IProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from 'app/entities/project-code';
import { ITaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from 'app/entities/task-type';

@Component({
    selector: 'jhi-time-sheet-update',
    templateUrl: './time-sheet-update.component.html'
})
export class TimeSheetUpdateComponent implements OnInit {
    private _timeSheet: ITimeSheet;
    isSaving: boolean;

    departments: IDepartment[];

    agileteams: IAgileTeam[];

    projectcodes: IProjectCode[];

    tasktypes: ITaskType[];
    forDate: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private timeSheetService: TimeSheetService,
        private departmentService: DepartmentService,
        private agileTeamService: AgileTeamService,
        private projectCodeService: ProjectCodeService,
        private taskTypeService: TaskTypeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ timeSheet }) => {
            this.timeSheet = timeSheet;
        });
        this.departmentService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<IDepartment[]>) => {
                if (!this.timeSheet.departmentId) {
                    this.departments = res.body;
                } else {
                    this.departmentService.find(this.timeSheet.departmentId).subscribe(
                        (subRes: HttpResponse<IDepartment>) => {
                            this.departments = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.agileTeamService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<IAgileTeam[]>) => {
                if (!this.timeSheet.agileTeamId) {
                    this.agileteams = res.body;
                } else {
                    this.agileTeamService.find(this.timeSheet.agileTeamId).subscribe(
                        (subRes: HttpResponse<IAgileTeam>) => {
                            this.agileteams = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectCodeService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<IProjectCode[]>) => {
                if (!this.timeSheet.projectCodeId) {
                    this.projectcodes = res.body;
                } else {
                    this.projectCodeService.find(this.timeSheet.projectCodeId).subscribe(
                        (subRes: HttpResponse<IProjectCode>) => {
                            this.projectcodes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.taskTypeService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<ITaskType[]>) => {
                if (!this.timeSheet.taskTypeId) {
                    this.tasktypes = res.body;
                } else {
                    this.taskTypeService.find(this.timeSheet.taskTypeId).subscribe(
                        (subRes: HttpResponse<ITaskType>) => {
                            this.tasktypes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.timeSheet.forDate = moment(this.forDate, DATE_TIME_FORMAT);
        if (this.timeSheet.id !== undefined) {
            this.subscribeToSaveResponse(this.timeSheetService.update(this.timeSheet));
        } else {
            this.subscribeToSaveResponse(this.timeSheetService.create(this.timeSheet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSheet>>) {
        result.subscribe((res: HttpResponse<ITimeSheet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackDepartmentById(index: number, item: IDepartment) {
        return item.id;
    }

    trackAgileTeamById(index: number, item: IAgileTeam) {
        return item.id;
    }

    trackProjectCodeById(index: number, item: IProjectCode) {
        return item.id;
    }

    trackTaskTypeById(index: number, item: ITaskType) {
        return item.id;
    }
    get timeSheet() {
        return this._timeSheet;
    }

    set timeSheet(timeSheet: ITimeSheet) {
        this._timeSheet = timeSheet;
        this.forDate = moment(timeSheet.forDate).format(DATE_TIME_FORMAT);
    }
}
