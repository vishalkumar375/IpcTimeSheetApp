import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectCode } from 'app/shared/model/project-code.model';

@Component({
    selector: 'jhi-project-code-detail',
    templateUrl: './project-code-detail.component.html'
})
export class ProjectCodeDetailComponent implements OnInit {
    projectCode: IProjectCode;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ projectCode }) => {
            this.projectCode = projectCode;
        });
    }

    previousState() {
        window.history.back();
    }
}
