import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IChatroom, Chatroom } from 'app/shared/model/chatroom.model';
import { ChatroomService } from './chatroom.service';

@Component({
  selector: 'jhi-chatroom-update',
  templateUrl: './chatroom-update.component.html'
})
export class ChatroomUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected chatroomService: ChatroomService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chatroom }) => {
      this.updateForm(chatroom);
    });
  }

  updateForm(chatroom: IChatroom): void {
    this.editForm.patchValue({
      id: chatroom.id,
      name: chatroom.name
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const chatroom = this.createFromForm();
    if (chatroom.id !== undefined) {
      this.subscribeToSaveResponse(this.chatroomService.update(chatroom));
    } else {
      this.subscribeToSaveResponse(this.chatroomService.create(chatroom));
    }
  }

  private createFromForm(): IChatroom {
    return {
      ...new Chatroom(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChatroom>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
