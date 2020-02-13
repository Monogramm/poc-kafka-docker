import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ApPoCTestModule } from '../../../test.module';
import { ChatroomComponent } from 'app/entities/chatroom/chatroom.component';
import { ChatroomService } from 'app/entities/chatroom/chatroom.service';
import { Chatroom } from 'app/shared/model/chatroom.model';

describe('Component Tests', () => {
  describe('Chatroom Management Component', () => {
    let comp: ChatroomComponent;
    let fixture: ComponentFixture<ChatroomComponent>;
    let service: ChatroomService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ApPoCTestModule],
        declarations: [ChatroomComponent]
      })
        .overrideTemplate(ChatroomComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ChatroomComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ChatroomService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Chatroom(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.chatrooms && comp.chatrooms[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
