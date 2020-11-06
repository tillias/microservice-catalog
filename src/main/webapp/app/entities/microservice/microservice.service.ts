import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMicroservice } from 'app/shared/model/microservice.model';

type EntityResponseType = HttpResponse<IMicroservice>;
type EntityArrayResponseType = HttpResponse<IMicroservice[]>;

@Injectable({ providedIn: 'root' })
export class MicroserviceService {
  public resourceUrl = SERVER_API_URL + 'api/microservices';

  constructor(protected http: HttpClient) {}

  create(microservice: IMicroservice): Observable<EntityResponseType> {
    return this.http.post<IMicroservice>(this.resourceUrl, microservice, { observe: 'response' });
  }

  update(microservice: IMicroservice): Observable<EntityResponseType> {
    return this.http.put<IMicroservice>(this.resourceUrl, microservice, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMicroservice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findAllById(ids: number[]): Observable<EntityArrayResponseType> {
    return this.http.get<IMicroservice[]>(`${this.resourceUrl}/by/${ids}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMicroservice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
