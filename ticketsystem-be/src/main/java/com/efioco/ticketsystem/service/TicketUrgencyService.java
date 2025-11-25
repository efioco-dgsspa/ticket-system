package com.efioco.ticketsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.TicketUrgencyDTO;
import com.efioco.ticketsystem.mapper.TicketUrgencyMapper;
import com.efioco.ticketsystem.repository.TicketUrgencyRepository;
import com.efioco.ticketsystem.response.TicketUrgencyResponse;

@Service
public class TicketUrgencyService implements TicketUrgencyServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(TicketTemplateService.class);

	@Autowired
	private TicketUrgencyRepository ticketUrgencyRepository;
	
	@Autowired
	private TicketUrgencyMapper ticketUrgencyMapper;
	
	@Override
    public TicketUrgencyResponse getAllTicketUrgencies() {
		logger.info("### Inizio processo di recupero di tutti i gradi di priorità relativi i ticket presenti nel sistema ###");
		TicketUrgencyResponse response = new TicketUrgencyResponse();
		List<TicketUrgencyDTO> urgencies = ticketUrgencyMapper.toDTOList(
				ticketUrgencyRepository.findAll());

		response.setUrgencies(urgencies);
		
		logger.info("### Recupero di tutti i gradi di priorità relativi i ticket presenti nel sistema completato con successo ###");
		return response;
	}
}
