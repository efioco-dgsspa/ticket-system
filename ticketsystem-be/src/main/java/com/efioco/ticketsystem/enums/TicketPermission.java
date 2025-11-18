package com.efioco.ticketsystem.enums;

public enum TicketPermission {

	TICKET_VIEW,
    TICKET_CREATE,
    TICKET_UPDATE,
    TICKET_DELETE;
	
	/**
     * Restituisce il nome del ruolo nel formato Spring Security (ROLE_XYZ)
     */
    public String asSpringRole() {
        return "ROLE_" + this.name();
    }
}
