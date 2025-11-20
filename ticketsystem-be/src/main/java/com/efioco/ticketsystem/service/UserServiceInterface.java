package com.efioco.ticketsystem.service;

import java.util.Set;

import com.efioco.ticketsystem.entity.RoleEntity;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.request.AuthRequest;
import com.efioco.ticketsystem.request.UserRequest;
import com.efioco.ticketsystem.response.UserResponse;

public interface UserServiceInterface {

	void createUserIfNotExists(String username, String email, String password, Set<RoleEntity> roles) throws UserServiceException;
	UserResponse createUser(UserRequest userRequest) throws UserServiceException;
	UserResponse getUserByUsername(String username) throws UserServiceException;
	UserResponse getUserByEmail(String email) throws UserServiceException;
	UserResponse getAllUsers();
	UserResponse updateUser(UserRequest userRequest) throws UserServiceException;
	void deleteUser(String id) throws UserServiceException;
	UserResponse getUserByEmailOrUsername(AuthRequest request) throws UserServiceException;
	UserResponse updateUserActiveStatus(String id, boolean active) throws UserServiceException;
	UserResponse searchUsers(UserRequest request) throws UserServiceException;
	
}
