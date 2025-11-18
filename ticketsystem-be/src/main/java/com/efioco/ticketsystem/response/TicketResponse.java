package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.TicketDTO;

public class TicketResponse extends PaginationResponse {

	private List<TicketDTO> tickets;
	private TicketDTO ticket;

	public List<TicketDTO> getTickets() {
		return tickets;
	}

	public void setTickets(List<TicketDTO> tickets) {
		this.tickets = tickets;
	}

	public TicketDTO getTicket() {
		return ticket;
	}

	public void setTicket(TicketDTO ticket) {
		this.ticket = ticket;
	}
	
}
