package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.CategoryDTO;
import com.efioco.ticketsystem.entity.CategoryEntity;

@Component
public class CategoryMapper {

	public CategoryDTO toDTO(CategoryEntity category) {
		if (category == null)
			return null;

		CategoryDTO dto = new CategoryDTO();
		dto.setId(category.getId().toString());
		dto.setCode(category.getCode());
		dto.setName(category.getName());
		dto.setDescription(category.getDescription());
		return dto;
	}
	
	public CategoryEntity toEntity(CategoryDTO dto) {
		if (dto == null)
			return null;

		CategoryEntity category = new CategoryEntity();
		category.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		category.setCode(dto.getCode());
		category.setName(dto.getName());
		category.setDescription(dto.getDescription());
		return category;
	}

	public List<CategoryDTO> toDTOList(List<CategoryEntity> categories) {
		if (CollectionUtils.isEmpty(categories)) return null;
        return categories.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<CategoryEntity> toEntityList(List<CategoryDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
	}
	
}
