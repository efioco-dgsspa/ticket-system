package com.efioco.ticketsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.efioco.ticketsystem.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================================
    //  ECCEZIONI PERSONALIZZATE
    // ============================================================

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserService(UserServiceException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(TicketServiceException.class)
    public ResponseEntity<ErrorResponse> handleTicketService(TicketServiceException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ============================================================
    //  NOT FOUND (ID non trovato / Risorse assenti)
    // ============================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // ============================================================
    //  VALIDAZIONE E REQUEST ERRATE
    // ============================================================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return buildError(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJson(HttpMessageNotReadableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Formato JSON non valido o campi non corretti.");
    }

    // ============================================================
    //  AUTENTICAZIONE E AUTORIZZAZIONE
    // ============================================================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenziali non valide");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccessDeniedException ex) {
        return buildError(HttpStatus.FORBIDDEN, "Accesso negato: permessi insufficienti.");
    }

    // ============================================================
    //  FALLBACK GENERALE
    // ============================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace(); // 🔥 utile in sviluppo, rimuovere in produzione
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Errore interno del server");
    }

    // ============================================================
    //  UTILITIES
    // ============================================================

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(String.valueOf(status.value()));
        error.setCustomerMessage(message);
        return ResponseEntity.status(status).body(error);
    }
}
