package com.efioco.ticketsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.TicketTemplateDTO;
import com.efioco.ticketsystem.mapper.TicketTemplateMapper;
import com.efioco.ticketsystem.repository.TicketTemplateRepository;
import com.efioco.ticketsystem.response.TicketTemplateResponse;

@Service
public class TicketTemplateService implements TicketTemplateServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(TicketTemplateService.class);

	@Autowired
	private TicketTemplateRepository ticketTemplateRepository;
	
	@Autowired
	private TicketTemplateMapper ticketTemplateMapper;
	
	@Override
    public TicketTemplateResponse getAllTicketTemplates() {
		logger.info("### Inizio processo di recupero di tutti i templates per i ticket presenti nel sistema ###");
		TicketTemplateResponse response = new TicketTemplateResponse();
		List<TicketTemplateDTO> templates = ticketTemplateMapper.toDTOList(
				ticketTemplateRepository.findAll());

		response.setTicketTemplates(templates);
		
		logger.info("### Recupero di tutti i templates per i ticket presenti nel sistema completato con successo ###");
		return response;
	}
}
