import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITimeSheet } from 'app/shared/model/time-sheet.model';

type EntityResponseType = HttpResponse<ITimeSheet>;
type EntityArrayResponseType = HttpResponse<ITimeSheet[]>;

@Injectable({ providedIn: 'root' })
export class TimeSheetService {
    private resourceUrl = SERVER_API_URL + 'api/time-sheets';

    constructor(private http: HttpClient) { }

    create(timeSheet: ITimeSheet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(timeSheet);
        return this.http
            .post<ITimeSheet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(timeSheet: ITimeSheet): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(timeSheet);
        return this.http
            .put<ITimeSheet>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<ITimeSheet>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    export(org: string, startDate: string, endDate: string): Observable<any> {
        return this.http
            .get<ITimeSheet>(`${this.resourceUrl}/export/${org}/${startDate}/${endDate}`, { observe: 'response' })
            .pipe(map((res: any) => this.convertDateArrayFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<ITimeSheet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(timeSheet: ITimeSheet): ITimeSheet {
        const copy: ITimeSheet = Object.assign({}, timeSheet, {
            forDate: timeSheet.forDate != null && timeSheet.forDate.isValid() ? timeSheet.forDate.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.forDate = res.body.forDate != null ? moment(res.body.forDate) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((timeSheet: ITimeSheet) => {
            timeSheet.forDate = timeSheet.forDate != null ? moment(timeSheet.forDate) : null;
        });
        return res;
    }
}
