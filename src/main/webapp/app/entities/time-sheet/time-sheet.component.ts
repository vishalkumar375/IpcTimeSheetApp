import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { IUser, UserService, Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { TimeSheetService } from './time-sheet.service';
import { Angular5Csv } from 'angular5-csv/Angular5-csv';
import * as moment from 'moment';
@Component({
    selector: 'jhi-time-sheet',
    templateUrl: './time-sheet.component.html',
    styleUrls: ['timesheet.scss']
})
export class TimeSheetComponent implements OnInit, OnDestroy {
    timeSheets: ITimeSheet[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    users: IUser[];
    user: any;
    startDate: string = moment().format('YYYY-MM-01');
    endDate: string = moment().format('YYYY-MM-') + moment().daysInMonth();
    exportOptions = {
        fieldSeparator: ',',
        quoteStrings: '"',
        decimalseparator: '.',
        showLabels: true,
        showTitle: false,
        useBom: true,
        noDownload: false,
        headers: ['Emp ID', 'Full Name', 'Department', 'Agile Team', 'Date', 'Day', 'Project Code', 'Task Type', 'Actual Hour', 'Comments']
    };

    constructor(
        private timeSheetService: TimeSheetService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private principal: Principal,
        private userService: UserService
    ) {
        this.timeSheets = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
    }

    exportTS(toDownload: boolean) {
        if (this.startDate && this.endDate) {
            const exportRequest = {
                org: this.currentAccount.organization.name,
                startDate: moment(this.startDate),
                endDate: moment(this.endDate),
                user: this.user === 'All' ? null : this.user
            };
            if (exportRequest.startDate.isAfter(exportRequest.endDate)) {
                alert('Start Date cannot be after End Date');
            } else {
                this.timeSheetService.export(exportRequest).subscribe(
                    (res: HttpResponse<ITimeSheet[]>) => {
                        if (toDownload) {
                            this.downloadFileSuccess(res.body);
                        } else {
                            this.timeSheets = res.body;
                        }
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        } else {
            alert('Please enter dates for export.');
        }
    }

    private downloadFileSuccess(timesheets: ITimeSheet[]) {
        const exportData = [];

        timesheets.forEach(ts => {
            const exportObj = {
                empId: ts.user.empId,
                fullName: ts.user.firstName + ' ' + ts.user.lastName,
                department: ts.user.department.departmentName,
                agileTeam: ts.user.agileTeam.teamName,
                forDate: moment(ts.forDate)
                    .local()
                    .format('D-MMM-YYYY'),
                forDay: moment(ts.forDate)
                    .local()
                    .format('ddd'),
                projectCode: ts.projectCode.projectCode,
                taskType: ts.taskType.taskType,
                actualHours: ts.actualHours,
                comments: ts.comments
            };
            exportData.push(exportObj);
        });
        const fileName = 'timesheet##' + this.startDate + '##to#' + this.endDate;
        const angularCsv = new Angular5Csv(exportData, fileName, this.exportOptions);
    }
    downloadFileError(res: Response) {
        console.log('error', res);
    }

    loadAll() {
        if (this.startDate && this.endDate) {
            const exportRequest = {
                org: this.currentAccount.organization.name,
                startDate: moment(this.startDate),
                endDate: moment(this.endDate),
                user: this.user === 'All' ? null : this.user
            };
            this.timeSheetService.export(exportRequest).subscribe(
                (res: HttpResponse<ITimeSheet[]>) => {
                    this.timeSheets = res.body;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        } else {
            this.timeSheetService
                .query({
                    page: this.page,
                    size: this.itemsPerPage,
                    sort: this.sort()
                })
                .subscribe(
                    (res: HttpResponse<ITimeSheet[]>) => this.paginateTimeSheets(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }

    reset() {
        this.page = 0;
        this.timeSheets = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    compareByOptionId(idFist, idSecond) {
        return idFist && idSecond && idFist.id === idSecond.id;
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;

            if (this.currentAccount && this.currentAccount.authorities.indexOf('ROLE_ADMIN') !== -1) {
                this.userService.queryForTimeSheet().subscribe(
                    (res: HttpResponse<IUser[]>) => {
                        this.loadAll();
                        this.user = 'All';
                        this.users = res.body;
                        this.users = this.users.sort((u1, u2): number => {
                            return u1.login > u2.login ? 1 : -1;
                        });
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        });
        this.registerChangeInTimeSheets();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITimeSheet) {
        return item.id;
    }

    registerChangeInTimeSheets() {
        this.eventSubscriber = this.eventManager.subscribe('timeSheetListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateTimeSheets(data: ITimeSheet[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.timeSheets.push(data[i]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
