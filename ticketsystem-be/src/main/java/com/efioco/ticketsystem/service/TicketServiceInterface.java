package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.exceptions.TicketServiceException;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.request.TicketRequest;
import com.efioco.ticketsystem.request.UserRequest;
import com.efioco.ticketsystem.response.TicketResponse;
import com.efioco.ticketsystem.response.UserResponse;

public interface TicketServiceInterface {

	TicketResponse getAllTickets();
    TicketResponse createTicket(TicketRequest ticketRequest) throws TicketServiceException;

}
