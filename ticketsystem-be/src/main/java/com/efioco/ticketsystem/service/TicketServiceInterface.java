package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.exceptions.TicketServiceException;
import com.efioco.ticketsystem.request.TicketRequest;
import com.efioco.ticketsystem.response.TicketResponse;

import java.util.UUID;

public interface TicketServiceInterface {

	TicketResponse getAllTickets();
    TicketResponse createTicket(TicketRequest ticketRequest) throws TicketServiceException;
    TicketResponse getTicketById(UUID id) throws TicketServiceException;
    TicketResponse searchTickets(TicketRequest request) throws TicketServiceException;

}
