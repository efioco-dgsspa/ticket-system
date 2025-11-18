package com.efioco.ticketsystem.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.TicketDTO;
import com.efioco.ticketsystem.mapper.TicketMapper;
import com.efioco.ticketsystem.repository.TicketRepository;
import com.efioco.ticketsystem.response.TicketResponse;

@Service
public class TicketService implements TicketServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TicketMapper ticketMapper;
	
	@Override
    public TicketResponse getAllTickets() {
		logger.info("### Inizio processo di recupero di tutti i tickets presenti nel sistema ###");
		TicketResponse response = new TicketResponse();
		List<TicketDTO> tickets = ticketRepository.findAll().stream()
	            .map(ticketMapper::toDTO)
	            .collect(Collectors.toList());

		response.setTickets(tickets);
		
		logger.info("### Recupero di tutti i tickets presenti nel sistema completato con successo ###");
		return response;
	}
}
