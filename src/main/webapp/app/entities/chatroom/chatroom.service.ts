import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IChatroom } from 'app/shared/model/chatroom.model';

type EntityResponseType = HttpResponse<IChatroom>;
type EntityArrayResponseType = HttpResponse<IChatroom[]>;

@Injectable({ providedIn: 'root' })
export class ChatroomService {
  public resourceUrl = SERVER_API_URL + 'api/chatrooms';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/chatrooms';

  constructor(protected http: HttpClient) {}

  create(chatroom: IChatroom): Observable<EntityResponseType> {
    return this.http.post<IChatroom>(this.resourceUrl, chatroom, { observe: 'response' });
  }

  update(chatroom: IChatroom): Observable<EntityResponseType> {
    return this.http.put<IChatroom>(this.resourceUrl, chatroom, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IChatroom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChatroom[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IChatroom[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
