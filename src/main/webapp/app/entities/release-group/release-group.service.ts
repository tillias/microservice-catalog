import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReleaseGroup } from 'app/shared/model/release-group.model';

type EntityResponseType = HttpResponse<IReleaseGroup>;
type EntityArrayResponseType = HttpResponse<IReleaseGroup[]>;

@Injectable({ providedIn: 'root' })
export class ReleaseGroupService {
  public resourceUrl = SERVER_API_URL + 'api/release-groups';

  constructor(protected http: HttpClient) {}

  create(releaseGroup: IReleaseGroup): Observable<EntityResponseType> {
    return this.http.post<IReleaseGroup>(this.resourceUrl, releaseGroup, { observe: 'response' });
  }

  update(releaseGroup: IReleaseGroup): Observable<EntityResponseType> {
    return this.http.put<IReleaseGroup>(this.resourceUrl, releaseGroup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReleaseGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReleaseGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
