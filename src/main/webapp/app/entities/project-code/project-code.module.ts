import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { IpcTimeSheetAppSharedModule } from 'app/shared';
import {
    ProjectCodeComponent,
    ProjectCodeDetailComponent,
    ProjectCodeUpdateComponent,
    ProjectCodeDeletePopupComponent,
    ProjectCodeDeleteDialogComponent,
    projectCodeRoute,
    projectCodePopupRoute
} from './';

const ENTITY_STATES = [...projectCodeRoute, ...projectCodePopupRoute];

@NgModule({
    imports: [IpcTimeSheetAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ProjectCodeComponent,
        ProjectCodeDetailComponent,
        ProjectCodeUpdateComponent,
        ProjectCodeDeleteDialogComponent,
        ProjectCodeDeletePopupComponent
    ],
    entryComponents: [ProjectCodeComponent, ProjectCodeUpdateComponent, ProjectCodeDeleteDialogComponent, ProjectCodeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IpcTimeSheetAppProjectCodeModule {}
