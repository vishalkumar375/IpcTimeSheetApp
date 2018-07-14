import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IpcTimeSheetAppSharedModule } from 'app/shared';
import {
    TaskTypeComponent,
    TaskTypeDetailComponent,
    TaskTypeUpdateComponent,
    TaskTypeDeletePopupComponent,
    TaskTypeDeleteDialogComponent,
    taskTypeRoute,
    taskTypePopupRoute
} from './';

const ENTITY_STATES = [...taskTypeRoute, ...taskTypePopupRoute];

@NgModule({
    imports: [IpcTimeSheetAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TaskTypeComponent,
        TaskTypeDetailComponent,
        TaskTypeUpdateComponent,
        TaskTypeDeleteDialogComponent,
        TaskTypeDeletePopupComponent
    ],
    entryComponents: [TaskTypeComponent, TaskTypeUpdateComponent, TaskTypeDeleteDialogComponent, TaskTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IpcTimeSheetAppTaskTypeModule {}
