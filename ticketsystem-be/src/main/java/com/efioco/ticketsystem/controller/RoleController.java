package com.efioco.ticketsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.response.ErrorResponse;
import com.efioco.ticketsystem.response.RoleResponse;
import com.efioco.ticketsystem.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*")
@Tag(name = "Role Controller", description = "Gestione CRUD dei ruoli")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	
	@GetMapping
    @Operation(
    	    summary = "Mostra tutti i ruoli presenti nel sistema",
    	    description = "Recupera e restituisce la lista completa di tutti i ruoli presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista dei ruoli restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun ruolo trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllRoles() {
    	try {
    		RoleResponse response = roleService.getAllRoles();
            if (CollectionUtils.isEmpty(response.getRoles())) {
                return buildError(HttpStatus.NOT_FOUND, "Nessun ruolo trovato");
            }
            return ResponseEntity.ok(response.getRoles());

        } catch (Exception e) {
            logger.error("Errore interno durante il recupero dei ruoli", e);
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
