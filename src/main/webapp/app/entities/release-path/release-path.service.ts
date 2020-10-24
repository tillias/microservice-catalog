import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReleasePath } from 'app/shared/model/release-path.model';

type EntityResponseType = HttpResponse<IReleasePath>;
type EntityArrayResponseType = HttpResponse<IReleasePath[]>;

@Injectable({ providedIn: 'root' })
export class ReleasePathService {
  public resourceUrl = SERVER_API_URL + 'api/release-paths';

  constructor(protected http: HttpClient) {}

  create(releasePath: IReleasePath): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releasePath);
    return this.http
      .post<IReleasePath>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(releasePath: IReleasePath): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(releasePath);
    return this.http
      .put<IReleasePath>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IReleasePath>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IReleasePath[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(releasePath: IReleasePath): IReleasePath {
    const copy: IReleasePath = Object.assign({}, releasePath, {
      createdOn: releasePath.createdOn && releasePath.createdOn.isValid() ? releasePath.createdOn.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((releasePath: IReleasePath) => {
        releasePath.createdOn = releasePath.createdOn ? moment(releasePath.createdOn) : undefined;
      });
    }
    return res;
  }
}
