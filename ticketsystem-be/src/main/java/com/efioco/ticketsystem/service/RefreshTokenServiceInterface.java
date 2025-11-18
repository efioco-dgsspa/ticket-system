package com.efioco.ticketsystem.service;

import java.util.Optional;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.RefreshTokenEntity;
import com.efioco.ticketsystem.entity.UserEntity;

public interface RefreshTokenServiceInterface {

	RefreshTokenEntity retrieveOrCreateRefreshToken(UserDTO user);
	Optional<RefreshTokenEntity> findByToken(String token);
	RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);
	void revokeToken(RefreshTokenEntity token);
	int revokeTokensByUser(UserEntity user);

}
