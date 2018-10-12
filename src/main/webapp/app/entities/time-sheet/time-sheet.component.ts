import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { Principal } from 'app/core';

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
    startDate: string;
    endDate: string;
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
        private principal: Principal
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

    exportTS() {
        if (this.startDate && this.endDate) {
            const exportRequest = {
                org: this.currentAccount.organization.name,
                startDate: moment(this.startDate),
                endDate: moment(this.endDate)
            };
            this.timeSheetService
                .export(exportRequest)
                .subscribe(
                    (res: HttpResponse<ITimeSheet[]>) => this.downloadFileSuccess(res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
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

    reset() {
        this.page = 0;
        this.timeSheets = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
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
