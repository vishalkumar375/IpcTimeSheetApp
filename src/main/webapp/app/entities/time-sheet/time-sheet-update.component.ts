import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { TimeSheetService } from './time-sheet.service';
import { ITaskType } from 'app/shared/model/task-type.model';
import { IProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from 'app/entities/project-code/project-code.service';
import { TaskTypeService } from 'app/entities/task-type';
import { IUser, UserService, Principal } from 'app/core';

@Component({
    selector: 'jhi-time-sheet-update',
    templateUrl: './time-sheet-update.component.html',
    styleUrls: ['timesheet.scss']
})
export class TimeSheetUpdateComponent implements OnInit {
    private _timeSheet: ITimeSheet;
    isSaving: boolean;

    tasktypes: ITaskType[];
    projectCodes: IProjectCode[];

    users: IUser[];
    loggedInUser: IUser;
    forDate: string;
    toCopyArray: any[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private timeSheetService: TimeSheetService,
        private taskTypeService: TaskTypeService,
        private userService: UserService,
        private principal: Principal,
        private projectCodeService: ProjectCodeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ timeSheet }) => {
            this.timeSheet = timeSheet;
        });
        this.taskTypeService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<ITaskType[]>) => {
                this.tasktypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectCodeService.query({ filter: 'timesheet-is-null' }).subscribe(
            (res: HttpResponse<IProjectCode[]>) => {
                this.projectCodes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.principal.identity(false).then(account => {
            this.loggedInUser = account;
            this.timeSheet.user = this.loggedInUser;
            if (this.loggedInUser && this.loggedInUser.authorities.indexOf('ROLE_ADMIN') !== -1) {
                this.userService.queryForTimeSheet().subscribe(
                    (res: HttpResponse<IUser[]>) => {
                        this.users = res.body;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        });
        this.populateCopyToArray(undefined);
    }

    populateCopyToArray($event) {
        this.toCopyArray = new Array<any>();
        const forDateMoment = $event ? moment($event, DATE_FORMAT) : moment(this.forDate, DATE_FORMAT);
        for (let i = 1; i < 8; i++) {
            const weekDay = this.getCurrentWeekDay(i, moment(forDateMoment));
            this.toCopyArray.push({
                isSelected: weekDay.isSame(forDateMoment),
                weekDate: weekDay,
                isDisabled: weekDay.isSame(forDateMoment)
            });
        }
    }

    getCurrentWeekDay(dayIndex, selectedForDate): Moment {
        const dateOfWeek: Moment = moment(selectedForDate.startOf('week').toDate());
        dateOfWeek.add(dayIndex, 'days');
        return dateOfWeek;
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.timeSheet.forDate = moment(this.forDate, DATE_FORMAT);
        const copyToDates: Moment[] = new Array<Moment>();
        this.toCopyArray.forEach(element => {
            if (!element.isDisabled && element.isSelected) {
                copyToDates.push(element.weekDate);
            }
        });
        this.timeSheet.copyToDates = copyToDates;
        console.log(this.timeSheet);
        if (this.timeSheet.id !== undefined) {
            this.subscribeToSaveResponse(this.timeSheetService.update(this.timeSheet));
        } else {
            this.subscribeToSaveResponse(this.timeSheetService.create(this.timeSheet));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ITimeSheet>>) {
        result.subscribe((res: HttpResponse<ITimeSheet>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError(res));
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError(res: HttpErrorResponse) {
        this.isSaving = false;
        alert(res.error.title);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    compareByOptionId(idFist, idSecond) {
        return idFist && idSecond && idFist.id === idSecond.id;
    }
    trackTaskTypeById(index: number, item: ITaskType) {
        return item.id;
    }
    trackProjectCodeById(index: number, item: IProjectCode) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }
    get timeSheet() {
        return this._timeSheet;
    }

    set timeSheet(timeSheet: ITimeSheet) {
        this._timeSheet = timeSheet;
        this.forDate = moment(timeSheet.forDate).format(DATE_FORMAT);
    }
}
