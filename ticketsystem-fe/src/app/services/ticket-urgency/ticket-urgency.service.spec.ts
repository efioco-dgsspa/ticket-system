import { TestBed } from '@angular/core/testing';

import { TicketUrgencyService } from './ticket-urgency.service';

describe('TicketUrgencyService', () => {
  let service: TicketUrgencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketUrgencyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
