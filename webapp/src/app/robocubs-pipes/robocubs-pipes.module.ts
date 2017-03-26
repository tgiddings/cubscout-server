import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {ArrayWithSizePipe} from "../array-with-size.pipe";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    ArrayWithSizePipe
  ],
  exports:[
    ArrayWithSizePipe
  ]
})
export class RobocubsPipesModule { }
