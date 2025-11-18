package com.efioco.ticketsystem.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ticket_statuses")
public class TicketStatusEntity {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code; // Esempio: "OPEN", "IN_PROGRESS", "WAITING_USER", "RESOLVED", "CLOSED"

    @Column(nullable = false)
    private String name; // Esempio: "Aperto", "In lavorazione", "In attesa utente", "Risolto", "Chiuso"

    @Column
    private String description; // opzionale, per dettagli interni o tooltip

    public TicketStatusEntity() {
    	
    }

    public TicketStatusEntity(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
