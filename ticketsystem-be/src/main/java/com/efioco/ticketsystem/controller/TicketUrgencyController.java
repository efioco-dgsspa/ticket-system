package com.efioco.ticketsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.response.TicketUrgencyResponse;
import com.efioco.ticketsystem.service.TicketUrgencyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ticket-urgencies")
@CrossOrigin(origins = "*")
@Tag(name = "Ticket Urgency Controller", description = "Gestione CRUD delle urgenze relative ai ticket")
public class TicketUrgencyController {

	private static final Logger logger = LoggerFactory.getLogger(TicketUrgencyController.class);

	@Autowired
	private TicketUrgencyService ticketUrgencyService;
	
	@GetMapping
    @Operation(
    	    summary = "Mostra tutti i gradi di priorità relativi i ticket presenti nel sistema",
    	    description = "Recupera e restituisce la lista completa di tutti i gradi di priorità relativi i ticket presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista dei gradi di priorità dei tickets restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun ruolo trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllTicketUrgencies() {
		TicketUrgencyResponse response = ticketUrgencyService.getAllTicketUrgencies();
    	return ResponseEntity.ok(response.getUrgencies());
    }
}
