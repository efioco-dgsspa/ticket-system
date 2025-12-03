package com.efioco.ticketsystem.controller;

import com.efioco.ticketsystem.response.ErrorResponse;
import com.efioco.ticketsystem.response.TicketResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.efioco.ticketsystem.response.TicketTemplateResponse;
import com.efioco.ticketsystem.service.TicketTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

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

    @GetMapping("/by-id-category/{id}")
    @Operation(
            summary = "Recupera sottocategorie dall'id categoria",
            description = "Restituisce le sottocategorie del ticket a partire da id categoria, fornito come parametro nel path."
    )
    @Parameter(
            name = "id",
            description = "L'id della categoria",
            required = true
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sottocategorie trovate e restituito correttamente"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida o formato errato"),
            @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
            @ApiResponse(responseCode = "404", description = "Sottocategorie non trovate"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getTicketTemplateByIdCategory(@PathVariable UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id non fornito");
        }

        TicketTemplateResponse response = ticketTemplateService.getAllTicketTemplatesByCategory(id);
        return ResponseEntity.ok(response.getTicketTemplates());
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(String.valueOf(status.value()));
        error.setCustomerMessage(message);
        return ResponseEntity.status(status).body(error);
    }

}
