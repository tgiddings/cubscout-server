import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FillScorecardComponent } from './fill-scorecard.component';

describe('FillScorecardComponent', () => {
  let component: FillScorecardComponent;
  let fixture: ComponentFixture<FillScorecardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FillScorecardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FillScorecardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
