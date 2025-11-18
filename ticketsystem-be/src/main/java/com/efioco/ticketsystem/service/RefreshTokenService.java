package com.efioco.ticketsystem.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.RefreshTokenEntity;
import com.efioco.ticketsystem.entity.UserEntity;
import com.efioco.ticketsystem.mapper.UserMapper;
import com.efioco.ticketsystem.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService implements RefreshTokenServiceInterface {
	
	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

	@Value("${jwt.refresh.expiration}")
	private Duration refreshTokenDuration;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public RefreshTokenEntity retrieveOrCreateRefreshToken(UserDTO user) {
    	logger.info("### Inizio processo di recupero o creazione del refresh token ###");
        Optional<RefreshTokenEntity> existingToken = 
        		refreshTokenRepository.findByUserId(UUID.fromString(user.getId()));

        if (existingToken.isPresent()) {
            RefreshTokenEntity token = existingToken.get();
            if (token.getExpiryDate().isAfter(Instant.now()) && BooleanUtils.isFalse(token.getRevoked())) {
            	
            	logger.info("### Refresh token ancora valido recuperato con successo ###");
                return token;
            }

            refreshTokenRepository.delete(token);
        }

        RefreshTokenEntity newToken = new RefreshTokenEntity();
        newToken.setUser(userMapper.toEntity(user));
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(Instant.now().plus(refreshTokenDuration));
        newToken.setRevoked(false);

        logger.info("### Refresh token inizializzato con successo ###");
        return refreshTokenRepository.save(newToken);
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token scaduto. Effettua di nuovo il login.");
        }
        return token;
    }

    @Override
    public void revokeToken(RefreshTokenEntity token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    @Override
    public int revokeTokensByUser(UserEntity user) {
        return refreshTokenRepository.deleteByUser(user);
    }
    
    @Scheduled(cron = "0 30 10 * * ?")
    public void cleanUpExpiredTokens() {
        logger.info("🧹 Avvio pulizia refresh token scaduti...");
        int deleted = refreshTokenRepository.deleteAllExpiredSince(Instant.now());
        logger.info("✅ Pulizia completata, rimossi {} token scaduti", deleted);
    }
}
