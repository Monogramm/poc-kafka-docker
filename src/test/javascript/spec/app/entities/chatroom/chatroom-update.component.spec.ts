import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ApPoCTestModule } from '../../../test.module';
import { ChatroomUpdateComponent } from 'app/entities/chatroom/chatroom-update.component';
import { ChatroomService } from 'app/entities/chatroom/chatroom.service';
import { Chatroom } from 'app/shared/model/chatroom.model';

describe('Component Tests', () => {
  describe('Chatroom Management Update Component', () => {
    let comp: ChatroomUpdateComponent;
    let fixture: ComponentFixture<ChatroomUpdateComponent>;
    let service: ChatroomService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ApPoCTestModule],
        declarations: [ChatroomUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ChatroomUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatroomUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatroomService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Chatroom(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Chatroom();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
