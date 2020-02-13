import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'chatroom',
        loadChildren: () => import('./chatroom/chatroom.module').then(m => m.ApPoCChatroomModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ApPoCEntityModule {}
