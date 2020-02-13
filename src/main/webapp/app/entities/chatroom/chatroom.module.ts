import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ApPoCSharedModule } from 'app/shared/shared.module';
import { ChatroomComponent } from './chatroom.component';
import { ChatroomDetailComponent } from './chatroom-detail.component';
import { ChatroomUpdateComponent } from './chatroom-update.component';
import { ChatroomDeleteDialogComponent } from './chatroom-delete-dialog.component';
import { chatroomRoute } from './chatroom.route';

@NgModule({
  imports: [ApPoCSharedModule, RouterModule.forChild(chatroomRoute)],
  declarations: [ChatroomComponent, ChatroomDetailComponent, ChatroomUpdateComponent, ChatroomDeleteDialogComponent],
  entryComponents: [ChatroomDeleteDialogComponent]
})
export class ApPoCChatroomModule {}
