import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IChatroom, Chatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';
import { ChatroomComponent } from './chatroom.component';
import { ChatroomDetailComponent } from './chatroom-detail.component';
import { ChatroomUpdateComponent } from './chatroom-update.component';

@Injectable({ providedIn: 'root' })
export class ChatroomResolve implements Resolve<IChatroom> {
  constructor(private service: ChatroomService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IChatroom> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((chatroom: HttpResponse<Chatroom>) => {
          if (chatroom.body) {
            return of(chatroom.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Chatroom());
  }
}

export const chatroomRoute: Routes = [
  {
    path: '',
    component: ChatroomComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Chatrooms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ChatroomDetailComponent,
    resolve: {
      chatroom: ChatroomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Chatrooms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ChatroomUpdateComponent,
    resolve: {
      chatroom: ChatroomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Chatrooms'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ChatroomUpdateComponent,
    resolve: {
      chatroom: ChatroomResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Chatrooms'
    },
    canActivate: [UserRouteAccessService]
  }
];
