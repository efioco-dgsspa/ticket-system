package com.efioco.ticketsystem.request;

import com.efioco.ticketsystem.dto.UserDTO;

public class UserRequest {

	private UserDTO user;

	public UserRequest() {
		
	}
	
	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
}
