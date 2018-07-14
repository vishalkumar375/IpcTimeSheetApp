import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TimeSheet } from 'app/shared/model/time-sheet.model';
import { TimeSheetService } from './time-sheet.service';
import { TimeSheetComponent } from './time-sheet.component';
import { TimeSheetDetailComponent } from './time-sheet-detail.component';
import { TimeSheetUpdateComponent } from './time-sheet-update.component';
import { TimeSheetDeletePopupComponent } from './time-sheet-delete-dialog.component';
import { ITimeSheet } from 'app/shared/model/time-sheet.model';

@Injectable({ providedIn: 'root' })
export class TimeSheetResolve implements Resolve<ITimeSheet> {
    constructor(private service: TimeSheetService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((timeSheet: HttpResponse<TimeSheet>) => timeSheet.body));
        }
        return of(new TimeSheet());
    }
}

export const timeSheetRoute: Routes = [
    {
        path: 'time-sheet',
        component: TimeSheetComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.timeSheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet/:id/view',
        component: TimeSheetDetailComponent,
        resolve: {
            timeSheet: TimeSheetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.timeSheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet/new',
        component: TimeSheetUpdateComponent,
        resolve: {
            timeSheet: TimeSheetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.timeSheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'time-sheet/:id/edit',
        component: TimeSheetUpdateComponent,
        resolve: {
            timeSheet: TimeSheetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.timeSheet.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const timeSheetPopupRoute: Routes = [
    {
        path: 'time-sheet/:id/delete',
        component: TimeSheetDeletePopupComponent,
        resolve: {
            timeSheet: TimeSheetResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'ipcTimeSheetApp.timeSheet.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
