package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.response.TicketTemplateResponse;

import java.util.UUID;

public interface TicketTemplateServiceInterface {

	TicketTemplateResponse getAllTicketTemplates();

    TicketTemplateResponse getAllTicketTemplatesByCategory(UUID idCategory);

}
