import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { IpcTimeSheetAppProjectCodeModule } from './project-code/project-code.module';
import { IpcTimeSheetAppAgileTeamModule } from './agile-team/agile-team.module';
import { IpcTimeSheetAppDepartmentModule } from './department/department.module';
import { IpcTimeSheetAppTaskTypeModule } from './task-type/task-type.module';
import { IpcTimeSheetAppTimeSheetModule } from './time-sheet/time-sheet.module';
import { IpcTimeSheetAppOrganizationModule } from './organization/organization.module';

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        IpcTimeSheetAppProjectCodeModule,
        IpcTimeSheetAppAgileTeamModule,
        IpcTimeSheetAppDepartmentModule,
        IpcTimeSheetAppTaskTypeModule,
        IpcTimeSheetAppTimeSheetModule,
        IpcTimeSheetAppOrganizationModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IpcTimeSheetAppEntityModule {}
