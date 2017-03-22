import {Component, OnInit, Input, Output} from '@angular/core';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.css']
})
export class RatingComponent implements OnInit {
  @Output()
  @Input()
  get rating(): number {
    return this._rating;
  }

  set rating(value: number) {
    this._rating = value;
  }
  get numStars(): number {
    return this._numStars;
  }

  @Input()
  set numStars(value: number) {
    this._numStars = value;
  }
  get label(): string {
    return this._label;
  }

  @Input()
  set label(value: string) {
    this._label = value;
  }
  private _label:string;

  private _numStars:number;

  private _rating:number;

  constructor() {
  }

  ngOnInit() {
  }

}
