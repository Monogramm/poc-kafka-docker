import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IChatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';

@Component({
  templateUrl: './chatroom-delete-dialog.component.html'
})
export class ChatroomDeleteDialogComponent {
  chatroom?: IChatroom;

  constructor(protected chatroomService: ChatroomService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chatroomService.delete(id).subscribe(() => {
      this.eventManager.broadcast('chatroomListModification');
      this.activeModal.close();
    });
  }
}
