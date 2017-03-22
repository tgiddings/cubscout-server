import { TestBed, inject } from '@angular/core/testing';

import { ScorecardService } from './scorecard.service';

describe('ScorecardService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ScorecardService]
    });
  });

  it('should ...', inject([ScorecardService], (service: ScorecardService) => {
    expect(service).toBeTruthy();
  }));
});
