package com.efioco.ticketsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.efioco.ticketsystem.response.CategoryResponse;
import com.efioco.ticketsystem.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
	
}
