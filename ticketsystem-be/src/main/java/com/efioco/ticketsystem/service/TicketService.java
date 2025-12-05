package com.efioco.ticketsystem.service;

import java.time.LocalDateTime;
import java.util.*;

import com.efioco.ticketsystem.entity.*;
import com.efioco.ticketsystem.exceptions.ResourceNotFoundException;
import com.efioco.ticketsystem.exceptions.TicketServiceException;
import com.efioco.ticketsystem.exceptions.UserServiceException;
import com.efioco.ticketsystem.mapper.CategoryMapper;
import com.efioco.ticketsystem.mapper.TicketMessageMapper;
import com.efioco.ticketsystem.mapper.TicketUrgencyMapper;
import com.efioco.ticketsystem.repository.TicketMessageRepository;
import com.efioco.ticketsystem.repository.TicketStatusRepository;
import com.efioco.ticketsystem.repository.UserRepository;
import com.efioco.ticketsystem.request.TicketRequest;
import com.efioco.ticketsystem.specification.TicketSpecification;
import com.efioco.ticketsystem.utility.CustomerIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.TicketDTO;
import com.efioco.ticketsystem.mapper.TicketMapper;
import com.efioco.ticketsystem.repository.TicketRepository;
import com.efioco.ticketsystem.response.TicketResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Service
public class TicketService implements TicketServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private TicketMapper ticketMapper;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TicketUrgencyMapper urgencyMapper;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketMessageRepository ticketMessageRepository;


    @Override
    public TicketResponse getAllTickets() {
		logger.info("### Inizio processo di recupero di tutti i tickets presenti nel sistema ###");
		TicketResponse response = new TicketResponse();
		List<TicketDTO> tickets = ticketMapper.toDTOList(ticketRepository.findAll());

		response.setTickets(tickets);
		
		logger.info("### Recupero di tutti i tickets presenti nel sistema completato con successo ###");
		return response;
	}

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest) throws TicketServiceException {
        logger.info("### Inizio processo di creazione ticket ###");
        TicketDTO ticketDTO = ticketRequest.getTicket();

        Assert.notNull(ticketDTO, "UserDTO non può essere null");
        Assert.hasText(ticketDTO.getTitle(), "Titolo obbligatorio");
        Assert.hasText(ticketDTO.getDescription(), "Descrizione obbligatoria");
        Assert.notNull(ticketDTO.getUrgency(), "Almeno un ruolo è obbligatorio");
        ticketDTO.setRemoved(false);

        TicketResponse response = new TicketResponse();

        TicketEntity ticketEntity = ticketMapper.toEntity(ticketDTO);
        ticketEntity.setTitle(ticketDTO.getTitle());
        ticketEntity.setDescription(ticketDTO.getDescription());

        TicketStatusEntity ticketStatusEntity = ticketStatusRepository.findByCode("OPEN")
                .orElseThrow(() -> new ResourceNotFoundException("Status non trovato."));
        ticketEntity.setStatus(ticketStatusEntity);

        UserEntity userEntity = userRepository.findById(UUID.fromString(ticketDTO.getCreatorId()))
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato."));
        ticketEntity.setCreator(userEntity);

        ticketEntity.setCategory(categoryMapper.toEntity(ticketDTO.getCategory()));
        ticketEntity.setUrgency(urgencyMapper.toEntity(ticketDTO.getUrgency()));
        ticketEntity.setCustomerId(CustomerIdGenerator.generate(ticketDTO.getCategory()));
        ticketEntity.setRemoved(false);
        ticketEntity.setCreatedAt(LocalDateTime.now());

        TicketMessageEntity msg = new TicketMessageEntity();
        msg.setTicket(ticketEntity);
        msg.setCreatedAt(LocalDateTime.now());
        msg.setAuthor(userEntity);
        msg.setDescription(ticketDTO.getFirstMessage());
        msg.setContent("-");
        ticketEntity.getMessages().add(msg);

        TicketEntity savedTicket = ticketRepository.save(ticketEntity);
        ticketMessageRepository.save(msg);

        response.setTicket(ticketMapper.toDTO(savedTicket));

        logger.info("### Creazione ticket completata con successo ###");
        return response;
    }

    @Override
    public TicketResponse getTicketById(UUID id) throws TicketServiceException {
        logger.info("### Inizio processo di recupero di un ticket a partire dal suo id ###");
        TicketEntity ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket non trovato con id: " + id));

        TicketResponse response = new TicketResponse();
        response.setTicket(ticketMapper.toDTO(ticket));

        logger.info("### Ticket recuperato con successo ###");
        return response;
    }

    @Override
    public TicketResponse searchTickets(TicketRequest request) throws TicketServiceException {
        TicketResponse response = new TicketResponse();
        try {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

            Page<TicketEntity> page = ticketRepository.findAll(new TicketSpecification(request), pageable);
            List<TicketEntity> entities = page.getContent();

            if (CollectionUtils.isEmpty(entities)) {
                response.setTotalPage(0);
                response.setTotalRows(0l);
                response.setTickets(null);

                return response;
            }

            response.setTotalPage(page.getTotalPages());
            response.setTotalRows(page.getTotalElements());

            response.setTickets(ticketMapper.toDTOList(entities));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new TicketServiceException("Errore durante il recupero dei ticket.");
        }
        return response;
    }
}
