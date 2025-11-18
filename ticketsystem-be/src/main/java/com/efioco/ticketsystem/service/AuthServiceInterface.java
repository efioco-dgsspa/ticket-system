package com.efioco.ticketsystem.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.exceptions.UserServiceException;

public interface AuthServiceInterface {

	UserDetails getUserDetails(UserDTO user) throws UserServiceException;
	void authenticate(String email, String password);

}
