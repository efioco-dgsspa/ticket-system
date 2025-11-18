package com.efioco.ticketsystem.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Body per login (può contenere username o email e password)")
public class AuthRequest {
    
	@Schema(description = "Username o Email dell'utente")
    private String identifier;
    
	@Schema(description = "Password dell'utente")
    private String password;

    public AuthRequest() {
    	
    }

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}
