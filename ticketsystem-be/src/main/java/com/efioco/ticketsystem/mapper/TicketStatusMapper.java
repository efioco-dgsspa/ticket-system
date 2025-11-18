package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketStatusDTO;
import com.efioco.ticketsystem.entity.TicketStatusEntity;

@Component
public class TicketStatusMapper {
	
	/**
	 * Mappa da Entity -> DTO
	 */
	public TicketStatusDTO toDTO(TicketStatusEntity tcktStatus) {
		if (tcktStatus == null)
			return null;

		TicketStatusDTO dto = new TicketStatusDTO();
		dto.setId(tcktStatus.getId().toString());
		dto.setCode(tcktStatus.getCode());
		dto.setName(tcktStatus.getName());
		dto.setDescription(tcktStatus.getDescription());
		return dto;
	}

	/**
	 * Mappa da DTO -> Entity
	 * già
	 */
	public TicketStatusEntity toEntity(TicketStatusDTO dto) {
		if (dto == null)
			return null;

		TicketStatusEntity tcktStatus = new TicketStatusEntity();
		tcktStatus.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		tcktStatus.setCode(dto.getCode());
		tcktStatus.setName(dto.getName());
		tcktStatus.setDescription(dto.getDescription());
		return tcktStatus;
	}

	/**
	 * Mappa lista di Entity -> lista di DTO
	 */
	public List<TicketStatusDTO> toDTOList(List<TicketStatusEntity> tcktStatuses) {
		if (CollectionUtils.isEmpty(tcktStatuses)) return null;
        return tcktStatuses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	/**
	 * Mappa lista di DTO -> lista di Entity
	 */
	public List<TicketStatusEntity> toEntityList(List<TicketStatusDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
	}
	
}
