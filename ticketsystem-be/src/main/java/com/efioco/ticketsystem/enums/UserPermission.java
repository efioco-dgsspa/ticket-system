package com.efioco.ticketsystem.enums;

public enum UserPermission {

	USER_VIEW,
    USER_CREATE,
    USER_UPDATE,
    USER_DELETE;
	
	/**
     * Restituisce il nome del ruolo nel formato Spring Security (ROLE_XYZ)
     */
    public String asSpringRole() {
        return "ROLE_" + this.name();
    }
}
