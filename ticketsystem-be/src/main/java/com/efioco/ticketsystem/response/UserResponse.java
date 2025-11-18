package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.UserDTO;

public class UserResponse {

	private UserDTO user;
	private List<UserDTO> users;
	
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public List<UserDTO> getUsers() {
		return users;
	}
	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	
}
