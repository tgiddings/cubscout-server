import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ResultsComponent, FilterSections, FilterResultScores} from "./results.component";
import {BrowserModule} from "@angular/platform-browser";
import {FormsModule} from "@angular/forms";
import {HttpModule, JsonpModule} from "@angular/http";
import {MaterialModule} from "@angular/material";

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    MaterialModule,
    CommonModule
  ],
  declarations: [
    ResultsComponent,
    FilterSections,
    FilterResultScores
  ],
  exports:[
    ResultsComponent
  ]
})
export class ResultsModule { }
