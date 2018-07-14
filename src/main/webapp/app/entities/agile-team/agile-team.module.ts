import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IpcTimeSheetAppSharedModule } from 'app/shared';
import {
    AgileTeamComponent,
    AgileTeamDetailComponent,
    AgileTeamUpdateComponent,
    AgileTeamDeletePopupComponent,
    AgileTeamDeleteDialogComponent,
    agileTeamRoute,
    agileTeamPopupRoute
} from './';

const ENTITY_STATES = [...agileTeamRoute, ...agileTeamPopupRoute];

@NgModule({
    imports: [IpcTimeSheetAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        AgileTeamComponent,
        AgileTeamDetailComponent,
        AgileTeamUpdateComponent,
        AgileTeamDeleteDialogComponent,
        AgileTeamDeletePopupComponent
    ],
    entryComponents: [AgileTeamComponent, AgileTeamUpdateComponent, AgileTeamDeleteDialogComponent, AgileTeamDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IpcTimeSheetAppAgileTeamModule {}
