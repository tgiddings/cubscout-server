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
import {ScoutComponent} from './scout/scout.component';
import {MatchService} from "./match.service";
import {ResultService} from "./result.service";
import {ResultsModule} from "./results/results.module";
import {ResultsComponent} from "./results/results.component";
import {RobotService} from "./robot.service";

const routs:Routes = [
  {path:'scout', component: ScoutComponent},
  {path:'results',component:ResultsComponent}
];

@NgModule({
  declarations: [
    AppComponent,
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
    ResultsModule,
    RouterModule.forRoot(routs)
  ],
  providers: [
    ScorecardService,
    GameService,
    EventService,
    ApiRootService,
    MatchService,
    ResultService,
    RobotService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
