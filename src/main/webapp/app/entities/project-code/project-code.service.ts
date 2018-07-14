import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjectCode } from 'app/shared/model/project-code.model';

type EntityResponseType = HttpResponse<IProjectCode>;
type EntityArrayResponseType = HttpResponse<IProjectCode[]>;

@Injectable({ providedIn: 'root' })
export class ProjectCodeService {
    private resourceUrl = SERVER_API_URL + 'api/project-codes';

    constructor(private http: HttpClient) {}

    create(projectCode: IProjectCode): Observable<EntityResponseType> {
        return this.http.post<IProjectCode>(this.resourceUrl, projectCode, { observe: 'response' });
    }

    update(projectCode: IProjectCode): Observable<EntityResponseType> {
        return this.http.put<IProjectCode>(this.resourceUrl, projectCode, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProjectCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjectCode[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
