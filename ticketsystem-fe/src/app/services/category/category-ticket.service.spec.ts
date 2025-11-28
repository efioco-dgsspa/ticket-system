import { TestBed } from '@angular/core/testing';

import { CategoryTicketService } from './category-ticket.service';

describe('CategoryTicketService', () => {
  let service: CategoryTicketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoryTicketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
