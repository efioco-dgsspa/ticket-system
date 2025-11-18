package com.efioco.ticketsystem.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.UserEntity;

@Component
public class UserMapper {

	@Autowired
	private RoleMapper roleMapper;
	
	/**
	 * Mappa da Entity -> DTO
	 */
	public UserDTO toDTO(UserEntity user) {
		if (user == null)
			return null;

		UserDTO dto = new UserDTO();
		dto.setId(user.getId().toString());
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setRoles(roleMapper.toDTOSet(user.getRoles()));
		dto.setUsername(user.getUsername());
		dto.setActive(user.getActive());
		return dto;
	}

	/**
	 * Mappa da DTO -> Entity
	 */
	public UserEntity toEntity(UserDTO dto) {
		if (dto == null)
			return null;

		UserEntity user = new UserEntity();
		user.setId(StringUtils.isNotBlank(dto.getId()) ? UUID.fromString(dto.getId()) : null);
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRoles(roleMapper.toEntitySet(dto.getRoles()));
		user.setUsername(dto.getUsername());
		user.setActive(dto.getActive());
		return user;
	}

	/**
	 * Mappa lista di Entity -> lista di DTO
	 */
	public List<UserDTO> toDTOList(List<UserEntity> users) {
		if (CollectionUtils.isEmpty(users)) return null;
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
	}

	/**
	 * Mappa lista di DTO -> lista di Entity
	 */
	public List<UserEntity> toEntityList(List<UserDTO> dtos) {
		if (CollectionUtils.isEmpty(dtos)) return null;
        return dtos.stream()
        		.map(this::toEntity)
                .collect(Collectors.toList());
	}
}
