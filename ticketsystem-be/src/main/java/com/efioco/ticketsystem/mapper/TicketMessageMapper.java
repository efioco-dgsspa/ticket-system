package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketMessageDTO;
import com.efioco.ticketsystem.entity.TicketMessageEntity;

@Component
public class TicketMessageMapper {
	
	@Autowired
	private TicketAttachmentMapper ticketAttachmentMapper;
	@Autowired
	@Lazy
	private TicketMapper ticketMapper;
	@Autowired
	private UserMapper userMapper;

	public TicketMessageDTO toDTO(TicketMessageEntity entity) {
		if (entity == null) return null;

		TicketMessageDTO dto = new TicketMessageDTO();
		dto.setId(entity.getId().toString());
		dto.setTicket(ticketMapper.toDTO(entity.getTicket()));
		dto.setAuthor(userMapper.toDTO(entity.getAuthor()));
		dto.setDescription(entity.getDescription());
		dto.setAttachments(ticketAttachmentMapper.toDTOList(entity.getAttachments()));
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		
		return dto;
	}
	
	public TicketMessageEntity toEntity(TicketMessageDTO dto) {
		if (dto == null) return null;

		TicketMessageEntity entity = new TicketMessageEntity();
		entity.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		entity.setTicket(ticketMapper.toEntity(dto.getTicket()));
		entity.setAuthor(userMapper.toEntity(dto.getAuthor()));
		entity.setDescription(dto.getDescription());
		entity.setAttachments(ticketAttachmentMapper.toEntityList(dto.getAttachments()));
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());
        
        return entity;
	}
	
	public List<TicketMessageDTO> toDTOList(List<TicketMessageEntity> ticketsMessages) {
		if (CollectionUtils.isEmpty(ticketsMessages)) return null;
        return ticketsMessages.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<TicketMessageEntity> toEntityList(List<TicketMessageDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
        		.map(this::toEntity)
                .collect(Collectors.toList());
	}
}
