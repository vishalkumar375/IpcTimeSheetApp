import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAgileTeam } from 'app/shared/model/agile-team.model';

type EntityResponseType = HttpResponse<IAgileTeam>;
type EntityArrayResponseType = HttpResponse<IAgileTeam[]>;

@Injectable({ providedIn: 'root' })
export class AgileTeamService {
    private resourceUrl = SERVER_API_URL + 'api/agile-teams';

    constructor(private http: HttpClient) {}

    create(agileTeam: IAgileTeam): Observable<EntityResponseType> {
        return this.http.post<IAgileTeam>(this.resourceUrl, agileTeam, { observe: 'response' });
    }

    update(agileTeam: IAgileTeam): Observable<EntityResponseType> {
        return this.http.put<IAgileTeam>(this.resourceUrl, agileTeam, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IAgileTeam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IAgileTeam[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
