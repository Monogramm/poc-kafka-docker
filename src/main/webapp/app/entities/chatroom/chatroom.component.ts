import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IChatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';
import { ChatroomDeleteDialogComponent } from './chatroom-delete-dialog.component';

@Component({
  selector: 'jhi-chatroom',
  templateUrl: './chatroom.component.html'
})
export class ChatroomComponent implements OnInit, OnDestroy {
  chatrooms?: IChatroom[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected chatroomService: ChatroomService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.chatroomService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IChatroom[]>) => (this.chatrooms = res.body || []));
      return;
    }

    this.chatroomService.query().subscribe((res: HttpResponse<IChatroom[]>) => (this.chatrooms = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInChatrooms();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IChatroom): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInChatrooms(): void {
    this.eventSubscriber = this.eventManager.subscribe('chatroomListModification', () => this.loadAll());
  }

  delete(chatroom: IChatroom): void {
    const modalRef = this.modalService.open(ChatroomDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.chatroom = chatroom;
  }
}
