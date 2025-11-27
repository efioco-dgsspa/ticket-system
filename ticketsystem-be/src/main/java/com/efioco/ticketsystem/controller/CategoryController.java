package com.efioco.ticketsystem.controller;

import com.efioco.ticketsystem.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.efioco.ticketsystem.response.CategoryResponse;
import com.efioco.ticketsystem.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
@Tag(name = "Category Controller", description = "Gestione CRUD delle categorie")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping
    @Operation(
    	    summary = "Mostra tutte le categorie presenti nel sistema",
    	    description = "Recupera e restituisce la lista completa di tutte le categorie presenti nel sistema."
    	)
    @ApiResponses({
    	@ApiResponse(responseCode = "200", description = "Lista delle categorie restituita correttamente"),
    	@ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
        @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
    	@ApiResponse(responseCode = "404", description = "Nessun ruolo trovato"),
    	@ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getAllCategories() {
    	CategoryResponse response = categoryService.getAllCategories();
    	return ResponseEntity.ok(response.getCategories());
    }

    @GetMapping("/by-id/{id}")
    @Operation(
            summary = "Recupera un singolo utente per id",
            description = "Restituisce i dettagli completi di una categoria a partire dal suo id, fornito come parametro nel path."
    )
    @Parameter(
            name = "id",
            description = "L'id della categoria da cercare",
            required = true
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria trovata e restituito correttamente"),
            @ApiResponse(responseCode = "400", description = "Richiesta non valida o formato errato"),
            @ApiResponse(responseCode = "401", description = "Token mancante, scaduto o non valido"),
            @ApiResponse(responseCode = "403", description = "Accesso negato - ruolo non autorizzato"),
            @ApiResponse(responseCode = "404", description = "Categoria non trovata"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id non fornito");
        }

        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response.getCategory());
    }
	
}
