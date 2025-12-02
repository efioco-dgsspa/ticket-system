package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.efioco.ticketsystem.dto.UserDTO;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketDTO;
import com.efioco.ticketsystem.entity.TicketEntity;

@Component
public class TicketMapper {

	@Autowired
	private TicketUrgencyMapper ticketUrgencyMapper;
	@Autowired
	private TicketMessageMapper ticketMessageMapper;
	@Autowired
	private TicketStatusMapper ticketStatusMapper;
	@Autowired
    private CategoryMapper categoryMapper;
	@Autowired
    private UserMapper userMapper;

    public TicketDTO toDTO(TicketEntity entity) {
        if (entity == null) return null;

        TicketDTO dto = new TicketDTO();
        dto.setId(entity.getId().toString());
        dto.setCustomerId(entity.getCustomerId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStatus(ticketStatusMapper.toDTO(entity.getStatus()));
        dto.setCategory(categoryMapper.toDTO(entity.getCategory()));
        dto.setCreator(userMapper.toDTO(entity.getCreator()));
        dto.setAssignedTo(userMapper.toDTO(entity.getAssignedTo()));
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setRemoved(entity.getRemoved());
        dto.setMessages(ticketMessageMapper.toDTOList(entity.getMessages()));
        dto.setUrgency(ticketUrgencyMapper.toDTO(entity.getUrgency()));

        return dto;
    }

    public TicketEntity toEntity(TicketDTO dto) {
        if (dto == null) return null;

        TicketEntity entity = new TicketEntity();
        entity.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
        entity.setCustomerId(dto.getCustomerId());
        entity.setTitle(dto.getTitle());
        entity.setStatus(ticketStatusMapper.toEntity(dto.getStatus()));
        entity.setCategory(categoryMapper.toEntity(dto.getCategory()));
        entity.setCreator(userMapper.toEntity(dto.getCreator()));
        entity.setAssignedTo(userMapper.toEntity(dto.getAssignedTo()));
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setRemoved(dto.getRemoved());
        entity.setUrgency(ticketUrgencyMapper.toEntity(dto.getUrgency()));
        
        return entity;
    }

    public List<TicketDTO> toDTOList(List<TicketEntity> tickets) {
		if (CollectionUtils.isEmpty(tickets)) return null;
        return tickets.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<TicketEntity> toEntityList(List<TicketDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
        		.map(this::toEntity)
                .collect(Collectors.toList());
	}
}
