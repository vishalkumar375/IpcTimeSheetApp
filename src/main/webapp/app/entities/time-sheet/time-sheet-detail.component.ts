import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';

@Component({
    selector: 'jhi-time-sheet-detail',
    templateUrl: './time-sheet-detail.component.html'
})
export class TimeSheetDetailComponent implements OnInit {
    timeSheet: ITimeSheet;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ timeSheet }) => {
            this.timeSheet = timeSheet;
        });
    }

    previousState() {
        window.history.back();
    }
}
