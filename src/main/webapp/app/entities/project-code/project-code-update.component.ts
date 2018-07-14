import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from './project-code.service';

@Component({
    selector: 'jhi-project-code-update',
    templateUrl: './project-code-update.component.html'
})
export class ProjectCodeUpdateComponent implements OnInit {
    private _projectCode: IProjectCode;
    isSaving: boolean;

    constructor(private projectCodeService: ProjectCodeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ projectCode }) => {
            this.projectCode = projectCode;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.projectCode.id !== undefined) {
            this.subscribeToSaveResponse(this.projectCodeService.update(this.projectCode));
        } else {
            this.subscribeToSaveResponse(this.projectCodeService.create(this.projectCode));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProjectCode>>) {
        result.subscribe((res: HttpResponse<IProjectCode>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get projectCode() {
        return this._projectCode;
    }

    set projectCode(projectCode: IProjectCode) {
        this._projectCode = projectCode;
    }
}
