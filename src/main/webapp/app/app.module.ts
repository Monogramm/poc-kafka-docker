import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ApPoCSharedModule } from 'app/shared/shared.module';
import { ApPoCCoreModule } from 'app/core/core.module';
import { ApPoCAppRoutingModule } from './app-routing.module';
import { ApPoCHomeModule } from './home/home.module';
import { ApPoCEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ApPoCSharedModule,
    ApPoCCoreModule,
    ApPoCHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ApPoCEntityModule,
    ApPoCAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class ApPoCAppModule {}
