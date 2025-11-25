package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.TicketTemplateDTO;
import com.efioco.ticketsystem.entity.TicketTemplateEntity;

@Component
public class TicketTemplateMapper {

	@Autowired
    private CategoryMapper categoryMapper;
	
	public TicketTemplateDTO toDTO(TicketTemplateEntity template) {
		if (template == null)
			return null;

		TicketTemplateDTO dto = new TicketTemplateDTO();
		dto.setId(template.getId().toString());
		dto.setCategory(categoryMapper.toDTO(template.getCategory()));
		dto.setName(template.getName());
		dto.setTemplateText(template.getTemplateText());
		return dto;
	}

	public TicketTemplateEntity toEntity(TicketTemplateDTO dto) {
		if (dto == null)
			return null;

		TicketTemplateEntity template = new TicketTemplateEntity();
		template.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		template.setCategory(categoryMapper.toEntity(dto.getCategory()));
		template.setName(dto.getName());
		template.setTemplateText(dto.getTemplateText());
		return template;
	}

	public List<TicketTemplateDTO> toDTOList(List<TicketTemplateEntity> templates) {
		if (CollectionUtils.isEmpty(templates)) return null;
        return templates.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<TicketTemplateEntity> toEntityList(List<TicketTemplateDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
	}
	
}
