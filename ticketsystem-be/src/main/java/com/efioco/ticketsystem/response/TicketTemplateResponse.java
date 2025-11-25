package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.TicketTemplateDTO;

public class TicketTemplateResponse {

	private List<TicketTemplateDTO> ticketTemplates;

	public List<TicketTemplateDTO> getTicketTemplates() {
		return ticketTemplates;
	}

	public void setTicketTemplates(List<TicketTemplateDTO> ticketTemplates) {
		this.ticketTemplates = ticketTemplates;
	}
	
}
