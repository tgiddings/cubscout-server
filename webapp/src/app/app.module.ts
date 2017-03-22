import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {HttpModule, JsonpModule} from '@angular/http';

import {AppComponent, sortScorecardSectionPipe} from './app.component';
import {MaterialModule} from "@angular/material";

import 'hammerjs'
import {ScorecardService} from "./scorecard.service";
import {GameService} from "./game.service";
import {EventService} from "./event.service";
import {ApiRootService} from "./api-root.service";
import { RatingComponent } from './rating/rating.component';
import { ArrayWithSizePipe } from './array-with-size.pipe';

@NgModule({
  declarations: [
    AppComponent,
    sortScorecardSectionPipe,
    RatingComponent,
    ArrayWithSizePipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    MaterialModule
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
