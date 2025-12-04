package com.efioco.ticketsystem.response;

import com.efioco.ticketsystem.dto.TicketStatusDTO;
import com.efioco.ticketsystem.dto.TicketUrgencyDTO;

import java.util.List;

public class TicketStatusResponse {

    private List<TicketStatusDTO> statuses;

    public List<TicketStatusDTO> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<TicketStatusDTO> statuses) {
        this.statuses = statuses;
    }
}
