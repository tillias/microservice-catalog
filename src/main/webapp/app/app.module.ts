import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { MicrocatalogSharedModule } from 'app/shared/shared.module';
import { MicrocatalogCoreModule } from 'app/core/core.module';
import { MicrocatalogAppRoutingModule } from './app-routing.module';
import { MicrocatalogHomeModule } from './home/home.module';
import { MicrocatalogEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MicrocatalogSharedModule,
    MicrocatalogCoreModule,
    MicrocatalogHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    MicrocatalogEntityModule,
    MicrocatalogAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class MicrocatalogAppModule {}
