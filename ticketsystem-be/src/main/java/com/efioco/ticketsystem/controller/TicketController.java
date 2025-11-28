package com.efioco.ticketsystem.controller;

import com.efioco.ticketsystem.request.TicketRequest;
import com.efioco.ticketsystem.request.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.efioco.ticketsystem.response.ErrorResponse;
import com.efioco.ticketsystem.response.TicketResponse;
import com.efioco.ticketsystem.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
@Tag(name = "Ticket Controller", description = "Gestione CRUD dei tickets")
public class TicketController {
	
	private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

	@Autowired
	private TicketService ticketService;

	@GetMapping
    @Operation(
    	    summary = "Mostra tutti i tickets presenti a sistema",
    	    description = "Recupera e restituisce la lista completa di tutti i tickets presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista tickets restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun ticket trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllTickets() {
    	try {
    		TicketResponse response = ticketService.getAllTickets();
            if (CollectionUtils.isEmpty(response.getTickets())) {
                return buildError(HttpStatus.NOT_FOUND, "Nessun ticket trovato");
            }
            return ResponseEntity.ok(response.getTickets());

        } catch (Exception e) {
            logger.error("Errore interno durante il recupero dei tickets", e);
            return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server");
        }
    }

    @PostMapping
    @Operation(
            summary = "Crea un nuovo ticket",
            description = "Crea un ticket a partire dai dati forniti nel body JSON e restituisce un JSON contenente il ticket appena creato.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "ticket creato correttamente"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida"),
            @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> createTicket(@RequestBody TicketRequest request) {
        var createdTicket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }
	
	private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(String.valueOf(status.value()));
        error.setCustomerMessage(message);
        return ResponseEntity.status(status).body(error);
    }
}
