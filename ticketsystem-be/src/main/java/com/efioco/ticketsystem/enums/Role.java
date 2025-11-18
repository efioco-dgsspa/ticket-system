package com.efioco.ticketsystem.enums;

public enum Role {
	ADMIN, 
	OPERATOR, 
	USER, 
	SUPPORT_MANAGER;
	
	/**
     * Restituisce il nome del ruolo nel formato Spring Security (ROLE_XYZ)
     */
    public String asSpringRole() {
        return "ROLE_" + this.name();
    }
}
