package com.efioco.ticketsystem.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.request.UserRequest;
import com.efioco.ticketsystem.response.UserResponse;
import com.efioco.ticketsystem.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Controller", description = "Gestione CRUD degli users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping
	@Operation(
			summary = "Crea un nuovo user", 
			description = "Crea uno user a partire dai dati forniti nel body JSON e restituisce un JSON contenente l user appena creato.")
	@ApiResponses({
	    @ApiResponse(responseCode = "201", description = "Utente creato correttamente"),
	    @ApiResponse(responseCode = "400", description = "Richiesta non valida"),
	    @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
	    @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
	    @ApiResponse(responseCode = "500", description = "Errore interno del server")
	})
	public ResponseEntity<?> createUser(@RequestBody UserRequest request) {
		var createdUser = userService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
    @GetMapping
    @Operation(
    	    summary = "Mostra tutti gli utenti a sistema",
    	    description = "Recupera e restituisce la lista completa di tutti gli utenti presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista utenti restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun utente trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllUsers() {
		UserResponse response = userService.getAllUsers();
		return ResponseEntity.ok(response.getUsers());
    }
    
    @PostMapping("/search-users")
    @Operation(
    	    summary = "Restituisce gli utenti in base al filtro",
    	    description = "Recupera e restituisce la lista degli utenti in base a quanto valorizzato nel filtro."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista utenti restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun utente trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> searchUsers(@RequestBody UserRequest request) {
    	UserResponse response = userService.searchUsers(request);
    	return ResponseEntity.ok(response.getUsers());
    }
    
    @GetMapping("/by-username/{username}")
    @Operation(
        summary = "Recupera un singolo utente per username",
        description = "Restituisce i dettagli completi di uno user a partire dal suo username, fornito come parametro nel path."
    )
    @Parameter(
    	name = "username",
        description = "Lo username dello user da cercare",
        example = "mario.rossi",
        required = true
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente trovato e restituito correttamente"),
        @ApiResponse(responseCode = "400", description = "Richiesta non valida o formato errato"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("Username non fornito");
		}
		
		UserResponse response = userService.getUserByUsername(username);
		return ResponseEntity.ok(response.getUser());
    }

    @GetMapping("/by-email/{email}")
    @Operation(
        summary = "Recupera un singolo utente per email",
        description = "Restituisce i dettagli completi di uno user a partire dalla sua email, fornita come parametro nel path."
    )
    @Parameter(
    	name = "email",
    	description = "L'indirizzo email dello user da cercare",
    	example = "mario.rossi@example.com",
    	required = true
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente trovato e restituito correttamente"),
        @ApiResponse(responseCode = "400", description = "Richiesta non valida o formato errato"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
    	if (StringUtils.isBlank(email)) {
    		throw new IllegalArgumentException("Email non fornito");
    	}
    	
    	UserResponse response = userService.getUserByEmail(email);
    	return ResponseEntity.ok(response.getUser());
    }
    
    @PutMapping("/update-user")
    @Operation(
        summary = "Modifica un utente esistente",
        description = "Aggiorna i dati di uno user (username, email, password, ruoli o stato) e invia una mail di notifica se la modifica è avvenuta con successo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati non validi o mancanti"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> updateUser(@RequestBody UserRequest request) {
    	var updatedUser = userService.updateUser(request);
		return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Elimina un utente solo se disattivo",
        description = "Rimuove un utente dal sistema, ma solo se il suo stato è impostato su disattivo (active = false)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente eliminato con successo"),
        @ApiResponse(responseCode = "400", description = "Utente non disattivo, impossibile eliminare"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
    	userService.deleteUser(id);
    	return ResponseEntity.ok("Utente eliminato con successo");
    }
    
    @PatchMapping("/{id}/activate")
    @Operation(
        summary = "Attiva un utente",
        description = "Imposta lo stato attivo di un utente su true, identificato dal suo ID, e invia una mail di notifica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente attivato correttamente"),
        @ApiResponse(responseCode = "400", description = "Richiesta non valida o utente già attivo"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> activateUser(@PathVariable String id) {
    	UserResponse response = userService.updateUserActiveStatus(id, true);
    	return ResponseEntity.ok(response.getUser());
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(
        summary = "Disattiva un utente",
        description = "Imposta lo stato attivo di un utente su false, identificato dal suo ID, e invia una mail di notifica."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Utente disattivato correttamente"),
        @ApiResponse(responseCode = "400", description = "Richiesta non valida o utente già disattivo"),
        @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> deactivateUser(@PathVariable String id) {
    	UserResponse response = userService.updateUserActiveStatus(id, false);
    	return ResponseEntity.ok(response.getUser());
    }

    @GetMapping("/by-role/{role}")
    @Operation(
            summary = "Mostra tutti gli utenti a partire dal ruolo inserito",
            description = "Recupera e restituisce la lista completa di tutti gli utenti con il ruolo ricercato."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista utenti restituita correttamente"),
            @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
            @ApiResponse(responseCode = "404", description = "Nessun utente trovato"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllUsersByRole(@PathVariable String role) {
        UserResponse response = userService.getUsersByRole(role.toUpperCase());
        return ResponseEntity.ok(response.getUsers());
    }

}
