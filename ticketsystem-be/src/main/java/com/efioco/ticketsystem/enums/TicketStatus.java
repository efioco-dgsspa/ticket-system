package com.efioco.ticketsystem.enums;

public enum TicketStatus {
	
	OPEN,          // Appena creato dall’utente
    IN_PROGRESS,   // Assegnato e in lavorazione da parte dell’operatore
    RESOLVED,      // Risolto, ma non ancora chiuso
    REOPENED,      // Riaperto dall’utente
    CLOSED;         // Chiuso definitivamente dall’admin
}
