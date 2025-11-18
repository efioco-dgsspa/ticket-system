package com.efioco.ticketsystem.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.efioco.ticketsystem.entity.RefreshTokenEntity;
import com.efioco.ticketsystem.entity.UserEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	
	Optional<RefreshTokenEntity> findByUserId(UUID userId);
	Optional<RefreshTokenEntity> findByToken(String token);
    int deleteByUser(UserEntity user);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity t WHERE t.expiryDate <= :now")
    int deleteAllExpiredSince(Instant now);
}
