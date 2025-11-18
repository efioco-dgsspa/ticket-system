package com.efioco.ticketsystem.response;

import com.efioco.ticketsystem.dto.UserDTO;

public class AuthResponse {

	private UserDTO user;
	private String accessToken;
    private String refreshToken;
    
	public AuthResponse(UserDTO user, String accessToken, String refreshToken) {
		this.user = user;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
}
