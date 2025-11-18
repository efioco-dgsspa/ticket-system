package com.efioco.ticketsystem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efioco.ticketsystem.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	@Query("SELECT u FROM UserEntity u WHERE u.username = :identifier OR u.email = :identifier")
	Optional<UserEntity> findByEmailOrUsername(String identifier);
	
	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
}
