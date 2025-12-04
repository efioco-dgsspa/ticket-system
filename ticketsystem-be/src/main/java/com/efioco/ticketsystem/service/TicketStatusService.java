package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.dto.TicketStatusDTO;
import com.efioco.ticketsystem.mapper.TicketStatusMapper;
import com.efioco.ticketsystem.repository.TicketStatusRepository;
import com.efioco.ticketsystem.response.TicketStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketStatusService implements TicketStatusServiceInterface{

    private static final Logger logger = LoggerFactory.getLogger(TicketStatusService.class);

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Autowired
    private TicketStatusMapper ticketStatusMapper;

    @Override
    public TicketStatusResponse getAllStatus() {
        logger.info("### Inizio processo di recupero di tutti gli stati dei ticket presenti nel sistema ###");
        TicketStatusResponse response = new TicketStatusResponse();
        List<TicketStatusDTO> statuses = ticketStatusMapper.toDTOList(ticketStatusRepository.findAll());

        response.setStatuses(statuses);

        logger.info("### Recupero di tutti gli stati dei ticket presenti nel sistema completato con successo ###");
        return response;
    }
}
