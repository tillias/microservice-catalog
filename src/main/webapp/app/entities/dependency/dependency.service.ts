import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDependency } from 'app/shared/model/dependency.model';

type EntityResponseType = HttpResponse<IDependency>;
type EntityArrayResponseType = HttpResponse<IDependency[]>;

@Injectable({ providedIn: 'root' })
export class DependencyService {
  public resourceUrl = SERVER_API_URL + 'api/dependencies';

  constructor(protected http: HttpClient) {}

  create(dependency: IDependency): Observable<EntityResponseType> {
    return this.http.post<IDependency>(this.resourceUrl, dependency, { observe: 'response' });
  }

  update(dependency: IDependency): Observable<EntityResponseType> {
    return this.http.put<IDependency>(this.resourceUrl, dependency, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDependency>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllById(ids: number[]): Observable<EntityArrayResponseType> {
    return this.http.get<IDependency[]>(`${this.resourceUrl}/by/${ids}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDependency[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
