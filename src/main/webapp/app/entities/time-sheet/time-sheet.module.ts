import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IpcTimeSheetAppSharedModule } from 'app/shared';
import { IpcTimeSheetAppAdminModule } from 'app/admin/admin.module';
import {
    TimeSheetComponent,
    TimeSheetDetailComponent,
    TimeSheetUpdateComponent,
    TimeSheetDeletePopupComponent,
    TimeSheetDeleteDialogComponent,
    timeSheetRoute,
    timeSheetPopupRoute
} from './';

const ENTITY_STATES = [...timeSheetRoute, ...timeSheetPopupRoute];

@NgModule({
    imports: [IpcTimeSheetAppSharedModule, IpcTimeSheetAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        TimeSheetComponent,
        TimeSheetDetailComponent,
        TimeSheetUpdateComponent,
        TimeSheetDeleteDialogComponent,
        TimeSheetDeletePopupComponent
    ],
    entryComponents: [TimeSheetComponent, TimeSheetUpdateComponent, TimeSheetDeleteDialogComponent, TimeSheetDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IpcTimeSheetAppTimeSheetModule {}
