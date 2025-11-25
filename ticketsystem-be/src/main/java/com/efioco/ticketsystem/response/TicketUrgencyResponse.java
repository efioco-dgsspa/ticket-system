package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.TicketUrgencyDTO;

public class TicketUrgencyResponse {

	private List<TicketUrgencyDTO> urgencies;

	public List<TicketUrgencyDTO> getUrgencies() {
		return urgencies;
	}

	public void setUrgencies(List<TicketUrgencyDTO> urgencies) {
		this.urgencies = urgencies;
	}
	
}
