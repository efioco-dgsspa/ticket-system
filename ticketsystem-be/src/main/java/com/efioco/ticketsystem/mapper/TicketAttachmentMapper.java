package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketAttachmentDTO;
import com.efioco.ticketsystem.entity.TicketAttachmentEntity;

@Component
public class TicketAttachmentMapper {
	
	@Autowired
	@Lazy
	private TicketMessageMapper ticketMessageMapper;

	public TicketAttachmentDTO toDTO(TicketAttachmentEntity entity) {
		if (entity == null) return null;

		TicketAttachmentDTO dto = new TicketAttachmentDTO();
		dto.setId(entity.getId().toString());	
		dto.setFilename(entity.getFilename());
		dto.setContentType(entity.getContentType());
		dto.setContent(entity.getContent());
		dto.setMessage(ticketMessageMapper.toDTO(entity.getMessage()));
		
        return dto;
	}
	
	public TicketAttachmentEntity toEntity(TicketAttachmentDTO dto) {
		if (dto == null) return null;

		TicketAttachmentEntity entity = new TicketAttachmentEntity();
		entity.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);	
		entity.setFilename(dto.getFilename());
		entity.setContentType(dto.getContentType());
		entity.setContent(dto.getContent());
		entity.setMessage(ticketMessageMapper.toEntity(dto.getMessage()));
        
        return entity;
	}
	
	public List<TicketAttachmentDTO> toDTOList(List<TicketAttachmentEntity> ticketsAttachments) {
		if (CollectionUtils.isEmpty(ticketsAttachments)) return null;
        return ticketsAttachments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<TicketAttachmentEntity> toEntityList(List<TicketAttachmentDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
        		.map(this::toEntity)
                .collect(Collectors.toList());
	}
}
