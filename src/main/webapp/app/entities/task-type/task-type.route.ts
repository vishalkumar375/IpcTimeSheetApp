import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { TaskType } from 'app/shared/model/task-type.model';
import { TaskTypeService } from './task-type.service';
import { TaskTypeComponent } from './task-type.component';
import { TaskTypeDetailComponent } from './task-type-detail.component';
import { TaskTypeUpdateComponent } from './task-type-update.component';
import { TaskTypeDeletePopupComponent } from './task-type-delete-dialog.component';
import { ITaskType } from 'app/shared/model/task-type.model';

@Injectable({ providedIn: 'root' })
export class TaskTypeResolve implements Resolve<ITaskType> {
    constructor(private service: TaskTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((taskType: HttpResponse<TaskType>) => taskType.body));
        }
        return of(new TaskType());
    }
}

export const taskTypeRoute: Routes = [
    {
        path: 'task-type',
        component: TaskTypeComponent,
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.taskType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'task-type/:id/view',
        component: TaskTypeDetailComponent,
        resolve: {
            taskType: TaskTypeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.taskType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'task-type/new',
        component: TaskTypeUpdateComponent,
        resolve: {
            taskType: TaskTypeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.taskType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'task-type/:id/edit',
        component: TaskTypeUpdateComponent,
        resolve: {
            taskType: TaskTypeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.taskType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskTypePopupRoute: Routes = [
    {
        path: 'task-type/:id/delete',
        component: TaskTypeDeletePopupComponent,
        resolve: {
            taskType: TaskTypeResolve
        },
        data: {
            authorities: ['ROLE_ADMIN'],
            pageTitle: 'ipcTimeSheetApp.taskType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
