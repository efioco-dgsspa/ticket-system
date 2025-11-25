package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.PermissionDTO;
import com.efioco.ticketsystem.entity.PermissionEntity;

@Component
public class PermissionMapper {
	
	public PermissionDTO toDTO(PermissionEntity permission) {
		if (permission == null)
			return null;

		PermissionDTO dto = new PermissionDTO();
		dto.setId(permission.getId().toString());
		dto.setName(permission.getName());
		return dto;
	}

	public PermissionEntity toEntity(PermissionDTO dto) {
		if (dto == null)
			return null;

		PermissionEntity permission = new PermissionEntity();
		permission.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		permission.setName(dto.getName());
		return permission;
	}

	public List<PermissionDTO> toDTOList(List<PermissionEntity> permissions) {
		if (CollectionUtils.isEmpty(permissions)) return null;
        return permissions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	public List<PermissionEntity> toEntityList(List<PermissionDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
	}
	
	public Set<PermissionEntity> toEntitySet(Set<PermissionDTO> dtos) {
	    if (CollectionUtils.isEmpty(dtos)) return null;
	    return dtos.stream()
	    		.map(this::toEntity)
	    		.collect(Collectors.toSet());
	}
	
	public Set<PermissionDTO> toDTOSet(Set<PermissionEntity> entities) {
	    if (CollectionUtils.isEmpty(entities)) return null;
	    return entities.stream()
	    		.map(this::toDTO)
	    		.collect(Collectors.toSet());
	}
}
