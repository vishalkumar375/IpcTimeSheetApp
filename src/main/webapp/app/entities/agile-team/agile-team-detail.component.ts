import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAgileTeam } from 'app/shared/model/agile-team.model';

@Component({
    selector: 'jhi-agile-team-detail',
    templateUrl: './agile-team-detail.component.html'
})
export class AgileTeamDetailComponent implements OnInit {
    agileTeam: IAgileTeam;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ agileTeam }) => {
            this.agileTeam = agileTeam;
        });
    }

    previousState() {
        window.history.back();
    }
}
