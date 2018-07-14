import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IAgileTeam } from 'app/shared/model/agile-team.model';
import { AgileTeamService } from './agile-team.service';

@Component({
    selector: 'jhi-agile-team-update',
    templateUrl: './agile-team-update.component.html'
})
export class AgileTeamUpdateComponent implements OnInit {
    private _agileTeam: IAgileTeam;
    isSaving: boolean;

    constructor(private agileTeamService: AgileTeamService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ agileTeam }) => {
            this.agileTeam = agileTeam;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.agileTeam.id !== undefined) {
            this.subscribeToSaveResponse(this.agileTeamService.update(this.agileTeam));
        } else {
            this.subscribeToSaveResponse(this.agileTeamService.create(this.agileTeam));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAgileTeam>>) {
        result.subscribe((res: HttpResponse<IAgileTeam>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get agileTeam() {
        return this._agileTeam;
    }

    set agileTeam(agileTeam: IAgileTeam) {
        this._agileTeam = agileTeam;
    }
}
