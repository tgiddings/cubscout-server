import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HttpModule, JsonpModule} from '@angular/http';
import { RouterModule, Routes } from '@angular/router';

import {AppComponent} from './app.component';
import {MaterialModule} from "@angular/material";

import 'hammerjs'
import {ScorecardService} from "./scorecard.service";
import {GameService} from "./game.service";
import {EventService} from "./event.service";
import {ApiRootService} from "./api-root.service";
import { RatingComponent } from './rating/rating.component';
import { ArrayWithSizePipe } from './array-with-size.pipe';
import { MakeScorecardComponent } from './make-scorecard/make-scorecard.component';
import {ScoutComponent, sortScorecardSectionPipe} from './scout/scout.component';

const routs:Routes = [
  {path:'fill_scorecard', component: ScoutComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    sortScorecardSectionPipe,
    RatingComponent,
    ArrayWithSizePipe,
    MakeScorecardComponent,
    ScoutComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    MaterialModule,
    RouterModule.forRoot(routs)
  ],
  providers: [
    ScorecardService,
    GameService,
    EventService,
    ApiRootService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
