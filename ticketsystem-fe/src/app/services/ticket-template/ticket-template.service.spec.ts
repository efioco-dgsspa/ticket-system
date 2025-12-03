import { TestBed } from '@angular/core/testing';

import { TicketTemplateService } from './ticket-template.service';

describe('TicketTemplateService', () => {
  let service: TicketTemplateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TicketTemplateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
