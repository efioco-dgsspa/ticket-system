package com.efioco.ticketsystem.controller;

import com.efioco.ticketsystem.response.ErrorResponse;
import com.efioco.ticketsystem.response.TicketStatusResponse;
import com.efioco.ticketsystem.service.TicketStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "*")
@Tag(name = "Ticket Status Controller", description = "Gestione CRUD degli stati dei ticket")
public class TicketStatusController {

    private static final Logger logger = LoggerFactory.getLogger(TicketStatusController.class);

    @Autowired
    private TicketStatusService ticketStatusService;

    @GetMapping
    @Operation(
            summary = "Mostra tutti gli stati dei ticket presenti a sistema",
            description = "Recupera e restituisce la lista completa di tutti gli stati dei ticket presenti nel sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista stati restituita correttamente"),
            @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
            @ApiResponse(responseCode = "404", description = "Nessuno stato trovato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllStatus() {
        try {
            TicketStatusResponse response = ticketStatusService.getAllStatus();
            if (CollectionUtils.isEmpty(response.getStatuses())) {
                return buildError(HttpStatus.NOT_FOUND, "Nessuno stato trovato");
            }
            return ResponseEntity.ok(response.getStatuses());

        } catch (Exception e) {
            logger.error("Errore interno durante il recupero degli stati", e);
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
