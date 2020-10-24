import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IReleaseStep } from 'app/shared/model/release-step.model';

type EntityResponseType = HttpResponse<IReleaseStep>;
type EntityArrayResponseType = HttpResponse<IReleaseStep[]>;

@Injectable({ providedIn: 'root' })
export class ReleaseStepService {
  public resourceUrl = SERVER_API_URL + 'api/release-steps';

  constructor(protected http: HttpClient) {}

  create(releaseStep: IReleaseStep): Observable<EntityResponseType> {
    return this.http.post<IReleaseStep>(this.resourceUrl, releaseStep, { observe: 'response' });
  }

  update(releaseStep: IReleaseStep): Observable<EntityResponseType> {
    return this.http.put<IReleaseStep>(this.resourceUrl, releaseStep, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReleaseStep>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReleaseStep[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
