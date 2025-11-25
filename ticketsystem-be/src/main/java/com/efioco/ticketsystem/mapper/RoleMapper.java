package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.RoleDTO;
import com.efioco.ticketsystem.entity.RoleEntity;

@Component
public class RoleMapper {

	@Autowired
	private PermissionMapper permissionMapper;
	
	public RoleDTO toDTO(RoleEntity role) {
		if (role == null)
			return null;

		RoleDTO dto = new RoleDTO();
		dto.setId(role.getId().toString());
		dto.setName(role.getName());
		dto.setPermissions(permissionMapper.toDTOSet(role.getPermissions()));
		return dto;
	}

	public RoleEntity toEntity(RoleDTO dto) {
		if (dto == null)
			return null;

		RoleEntity role = new RoleEntity();
		role.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		role.setName(dto.getName());
		role.setPermissions(permissionMapper.toEntitySet(dto.getPermissions()));
		return role;
	}

	public List<RoleDTO> toDTOList(List<RoleEntity> entities) {
		if (CollectionUtils.isEmpty(entities))
			return null;
		return entities.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	public List<RoleEntity> toEntityList(List<RoleDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos))
			return null;
		return dtos.stream()
				.map(this::toEntity)
				.collect(Collectors.toList());
	}
	
	public Set<RoleEntity> toEntitySet(Set<RoleDTO> dtos) {
	    if (CollectionUtils.isEmpty(dtos)) return null;
	    return dtos.stream()
	    		.map(this::toEntity)
	    		.collect(Collectors.toSet());
	}
	
	public Set<RoleDTO> toDTOSet(Set<RoleEntity> entities) {
	    if (CollectionUtils.isEmpty(entities)) return null;
	    return entities.stream()
	    		.map(this::toDTO)
	    		.collect(Collectors.toSet());
	}
}
