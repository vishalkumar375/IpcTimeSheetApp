import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department';
import { IAgileTeam } from 'app/shared/model/agile-team.model';
import { AgileTeamService } from 'app/entities/agile-team';
import { IProjectCode } from 'app/shared/model/project-code.model';
import { ProjectCodeService } from 'app/entities/project-code';
import { IOrganization } from 'app/shared/model/organization.model';
import { OrganizationService } from 'app/entities/organization';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';
import { Observable } from 'rxjs';

import { JhiLanguageHelper, User, UserService } from 'app/core';

@Component({
    selector: 'jhi-user-mgmt-update',
    templateUrl: './user-management-update.component.html'
})
export class UserMgmtUpdateComponent implements OnInit {
    user: User;
    languages: any[];
    authorities: any[];
    isSaving: boolean;
    departments: IDepartment[];
    organizations: IOrganization[];
    agileteams: IAgileTeam[];
    projectcodes: IProjectCode[];

    constructor(
        private languageHelper: JhiLanguageHelper,
        private jhiAlertService: JhiAlertService,
        private userService: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private departmentService: DepartmentService,
        private organizationService: OrganizationService,
        private agileTeamService: AgileTeamService,
        private projectCodeService: ProjectCodeService,
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.route.data.subscribe(({ user }) => {
            this.user = user.body ? user.body : user;
        });
        this.authorities = [];
        this.userService.authorities().subscribe(authorities => {
            this.authorities = authorities;
        });
        this.languageHelper.getAll().then(languages => {
            this.languages = languages;
        });
        this.departmentService.query({ filter: '' }).subscribe(
            (res: HttpResponse<IDepartment[]>) => {
                this.departments = res.body;
             },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.organizationService.query({ filter: '' }).subscribe(
            (res: HttpResponse<IOrganization[]>) => {
                this.organizations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.agileTeamService.query({ filter: '' }).subscribe(
            (res: HttpResponse<IAgileTeam[]>) => {
                this.agileteams = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.projectCodeService.query({ filter: '' }).subscribe(
            (res: HttpResponse<IProjectCode[]>) => {
                this.projectcodes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        this.router.navigate(['/admin/user-management']);
    }

    compareByOptionId(idFist, idSecond) {
        return idFist && idSecond && idFist.id === idSecond.id;
     }
    save() {
        this.isSaving = true;
        if (this.user.id !== null) {
            this.userService.update(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        } else {
            this.userService.create(this.user).subscribe(response => this.onSaveSuccess(response), () => this.onSaveError());
        }
    }

    private onSaveSuccess(result) {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
