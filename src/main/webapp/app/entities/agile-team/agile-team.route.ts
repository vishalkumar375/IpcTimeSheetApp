import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { AgileTeam } from 'app/shared/model/agile-team.model';
import { AgileTeamService } from './agile-team.service';
import { AgileTeamComponent } from './agile-team.component';
import { AgileTeamDetailComponent } from './agile-team-detail.component';
import { AgileTeamUpdateComponent } from './agile-team-update.component';
import { AgileTeamDeletePopupComponent } from './agile-team-delete-dialog.component';
import { IAgileTeam } from 'app/shared/model/agile-team.model';

@Injectable({ providedIn: 'root' })
export class AgileTeamResolve implements Resolve<IAgileTeam> {
    constructor(private service: AgileTeamService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((agileTeam: HttpResponse<AgileTeam>) => agileTeam.body));
        }
        return of(new AgileTeam());
    }
}

export const agileTeamRoute: Routes = [
    {
        path: 'agile-team',
        component: AgileTeamComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.agileTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agile-team/:id/view',
        component: AgileTeamDetailComponent,
        resolve: {
            agileTeam: AgileTeamResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.agileTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agile-team/new',
        component: AgileTeamUpdateComponent,
        resolve: {
            agileTeam: AgileTeamResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.agileTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'agile-team/:id/edit',
        component: AgileTeamUpdateComponent,
        resolve: {
            agileTeam: AgileTeamResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.agileTeam.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const agileTeamPopupRoute: Routes = [
    {
        path: 'agile-team/:id/delete',
        component: AgileTeamDeletePopupComponent,
        resolve: {
            agileTeam: AgileTeamResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.agileTeam.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
