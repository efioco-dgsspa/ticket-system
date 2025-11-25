package com.efioco.ticketsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.response.TicketTemplateResponse;
import com.efioco.ticketsystem.service.TicketTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ticket-templates")
@CrossOrigin(origins = "*")
@Tag(name = "Ticket Template Controller", description = "Gestione CRUD dei templates per i ticket")
public class TicketTemplateController {

	private static final Logger logger = LoggerFactory.getLogger(TicketTemplateController.class);

	@Autowired
	private TicketTemplateService ticketTemplateService;
	
	@GetMapping
    @Operation(
    	    summary = "Mostra tutti i templates per i ticket presenti nel sistema",
    	    description = "Recupera e restituisce la lista completa di tutti i templates per i ticket presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista dei templates restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun ruolo trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllTicketTemplates() {
		TicketTemplateResponse response = ticketTemplateService.getAllTicketTemplates();
    	return ResponseEntity.ok(response.getTicketTemplates());
    }

}
