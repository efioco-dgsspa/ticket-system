package com.efioco.ticketsystem.request;

import com.efioco.ticketsystem.dto.TicketDTO;

public class TicketRequest extends PaginationRequest {
	
	private TicketDTO ticket;

	public TicketDTO getTicket() {
		return ticket;
	}

	public void setTicket(TicketDTO ticket) {
		this.ticket = ticket;
	}
	
}
