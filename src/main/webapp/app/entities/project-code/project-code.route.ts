import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from './project-code.service';
import { ProjectCodeComponent } from './project-code.component';
import { ProjectCodeDetailComponent } from './project-code-detail.component';
import { ProjectCodeUpdateComponent } from './project-code-update.component';
import { ProjectCodeDeletePopupComponent } from './project-code-delete-dialog.component';
import { IProjectCode } from 'app/shared/model/project-code.model';

@Injectable({ providedIn: 'root' })
export class ProjectCodeResolve implements Resolve<IProjectCode> {
    constructor(private service: ProjectCodeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((projectCode: HttpResponse<ProjectCode>) => projectCode.body));
        }
        return of(new ProjectCode());
    }
}

export const projectCodeRoute: Routes = [
    {
        path: 'project-code',
        component: ProjectCodeComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.projectCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-code/:id/view',
        component: ProjectCodeDetailComponent,
        resolve: {
            projectCode: ProjectCodeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.projectCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-code/new',
        component: ProjectCodeUpdateComponent,
        resolve: {
            projectCode: ProjectCodeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.projectCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'project-code/:id/edit',
        component: ProjectCodeUpdateComponent,
        resolve: {
            projectCode: ProjectCodeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.projectCode.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const projectCodePopupRoute: Routes = [
    {
        path: 'project-code/:id/delete',
        component: ProjectCodeDeletePopupComponent,
        resolve: {
            projectCode: ProjectCodeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.projectCode.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
