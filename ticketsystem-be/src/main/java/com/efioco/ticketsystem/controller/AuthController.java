package com.efioco.ticketsystem.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.RefreshTokenEntity;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.mapper.UserMapper;
import com.efioco.ticketsystem.request.AuthRequest;
import com.efioco.ticketsystem.request.TokenRefreshRequest;
import com.efioco.ticketsystem.response.AuthResponse;
import com.efioco.ticketsystem.response.ErrorResponse;
import com.efioco.ticketsystem.security.JwtUtil;
import com.efioco.ticketsystem.service.AuthService;
import com.efioco.ticketsystem.service.RefreshTokenService;
import com.efioco.ticketsystem.service.UserService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "Gestione autenticazione JWT (login e registrazione)")
@CrossOrigin(origins = "*")
public class AuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private RefreshTokenService refreshTokenService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private AuthService authService;
	
	// 🔑 LOGIN
	@PostMapping("/login")
	@Operation(
			summary = "Esegue il login e genera token JWT + refresh token.",
			description = """
		        Permette a un utente registrato di autenticarsi usando email e password.
		        Se le credenziali sono valide, restituisce un access token JWT e un refresh token persistente.
		        """,
		    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AuthRequest.class)))
			)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Autenticazione riuscita, token generati correttamente"),
		@ApiResponse(responseCode = "400", description = "Richiesta non valida o formato errato"),
		@ApiResponse(responseCode = "401", description = "Credenziali non valide"),
		@ApiResponse(responseCode = "500", description = "Errore interno del server")
	})
	public ResponseEntity<?> login(HttpServletRequest httpServReq) throws IOException {
		AuthRequest request = objectMapper.readValue(httpServReq.getInputStream(), AuthRequest.class);
		try {
			
			UserDTO user = userService.getUserByEmailOrUsername(request).getUser();
			
			authService.authenticate(user.getEmail(),request.getPassword());

			String accessToken = jwtUtil.generateAccessToken(user);
			RefreshTokenEntity refreshToken = refreshTokenService.retrieveOrCreateRefreshToken(user);

            AuthResponse authResponse = new AuthResponse(user, accessToken, refreshToken.getToken());
            return ResponseEntity.ok(authResponse);

		} catch (BadCredentialsException e) {
            logger.warn("Credenziali non valide per l'utente: {}", request.getIdentifier());
            return buildError(HttpStatus.UNAUTHORIZED, "Credenziali non valide");

        } catch (UserServiceException e) {
            logger.error("Errore durante il recupero utente: {}", e);
            return buildError(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (Exception e) {
            logger.error("Errore interno durante il login", e);
            return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server");
        }
	}

	@PostMapping("/refresh")
	@Operation(
			summary = "Rigenera un nuovo access token usando il refresh token salvato a DB.",
			description = """
		        Valida il refresh token fornito e, se ancora valido, genera un nuovo access token JWT.
		        Il refresh token non viene rigenerato, ma riutilizzato fino alla sua scadenza.
		        """,
		    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TokenRefreshRequest.class)))
	)
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Nuovo access token generato correttamente"),
		@ApiResponse(responseCode = "400", description = "Richiesta non valida o token malformato"),
		@ApiResponse(responseCode = "401", description = "Refresh token non valido o scaduto"),
		@ApiResponse(responseCode = "500", description = "Errore interno del server")
	})
	public ResponseEntity<?> refresh(HttpServletRequest req) throws StreamReadException, DatabindException, IOException {
		TokenRefreshRequest request = objectMapper.readValue(req.getInputStream(), TokenRefreshRequest.class);
		try {
			logger.info("### Inizio processo di creazione nuovo access token, presentando il refresh token ###");
			String refreshTokenRequest = request.getRefreshToken();

			RefreshTokenEntity refreshToken = refreshTokenService.findByToken(refreshTokenRequest)
					.map(refreshTokenService::verifyExpiration)
					.orElseThrow(() -> new RuntimeException("Refresh token non valido o non trovato."));

			UserDTO user = userMapper.toDTO(refreshToken.getUser());
			
			String newAccessToken = jwtUtil.generateAccessToken(user);

			AuthResponse response = new AuthResponse(user, newAccessToken, refreshTokenRequest);
			
			logger.info("### Nuovo access token generato con successo ###");
            return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
            logger.warn("Errore refresh token: {}", e);
            return buildError(HttpStatus.UNAUTHORIZED, e.getMessage());

        } catch (Exception e) {
            logger.error("Errore interno durante il refresh del token", e);
            return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server");
        }
	}
	
	private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(String.valueOf(status.value()));
        error.setCustomerMessage(message);
        return ResponseEntity.status(status).body(error);
    }
}
