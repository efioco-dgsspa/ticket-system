package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketUrgencyDTO;
import com.efioco.ticketsystem.entity.TicketUrgencyEntity;

public class TicketUrgencyMapper {

	public TicketUrgencyDTO toDTO(TicketUrgencyEntity urgency) {
		if (urgency == null)
			return null;

		TicketUrgencyDTO dto = new TicketUrgencyDTO();
		dto.setId(urgency.getId().toString());
		dto.setName(urgency.getName());
		dto.setDescription(urgency.getDescription());
		dto.setPriority(urgency.getPriority());
		return dto;
	}

	public TicketUrgencyEntity toEntity(TicketUrgencyDTO dto) {
		if (dto == null)
			return null;

		TicketUrgencyEntity urgency = new TicketUrgencyEntity();
		urgency.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		urgency.setName(dto.getName());
		urgency.setDescription(dto.getDescription());
		urgency.setPriority(dto.getPriority());
		return urgency;
	}

	public List<TicketUrgencyDTO> toDTOList(List<TicketUrgencyEntity> urgencies) {
		if (CollectionUtils.isEmpty(urgencies)) return null;
        return urgencies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<TicketUrgencyEntity> toEntityList(List<TicketUrgencyDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
	}
}
