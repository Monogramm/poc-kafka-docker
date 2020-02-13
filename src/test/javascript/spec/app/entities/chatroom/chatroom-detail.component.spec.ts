import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApPoCTestModule } from '../../../test.module';
import { ChatroomDetailComponent } from 'app/entities/chatroom/chatroom-detail.component';
import { Chatroom } from 'app/shared/model/chatroom.model';

describe('Component Tests', () => {
  describe('Chatroom Management Detail Component', () => {
    let comp: ChatroomDetailComponent;
    let fixture: ComponentFixture<ChatroomDetailComponent>;
    const route = ({ data: of({ chatroom: new Chatroom(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ApPoCTestModule],
        declarations: [ChatroomDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ChatroomDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ChatroomDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load chatroom on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.chatroom).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
