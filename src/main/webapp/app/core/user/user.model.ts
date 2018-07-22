import { IAgileTeam } from 'app/shared/model/agile-team.model';
import { IProjectCode } from 'app/shared/model/project-code.model';
import { IDepartment } from 'app/shared/model/department.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authorities?: any[];
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    department?: IDepartment;
    agileTeam?: IAgileTeam;
    projectCode?: IProjectCode;
    organization?: IOrganization;
    empId?: number;
    personId?: number;
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authorities?: any[],
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public department?: IDepartment,
        public agileTeam?: IAgileTeam,
        public projectCode?: IProjectCode,
        public organization?: IOrganization,
        public empId?: number,
        public personId?: number
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authorities = authorities ? authorities : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.department = department ? department : null;
        this.organization = organization ? organization : null;
        this.agileTeam = agileTeam ? agileTeam : null;
        this.projectCode = projectCode ? projectCode : null;
        this.empId = empId ? empId : null;
        this.personId = personId ? personId : null;
    }
}
