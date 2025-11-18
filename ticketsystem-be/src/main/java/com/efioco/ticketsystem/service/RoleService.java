package com.efioco.ticketsystem.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.RoleDTO;
import com.efioco.ticketsystem.entity.PermissionEntity;
import com.efioco.ticketsystem.entity.RoleEntity;
import com.efioco.ticketsystem.mapper.RoleMapper;
import com.efioco.ticketsystem.repository.RoleRepository;
import com.efioco.ticketsystem.response.RoleResponse;

@Service
public class RoleService implements RoleServiceInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public RoleEntity findOrCreateRole(String name, Set<PermissionEntity> permissions) {
		return roleRepository
				.findByName(name)
				.orElseGet(() -> roleRepository.save(new RoleEntity(name, permissions)));
	}
	
	@Override
    public RoleResponse getAllRoles() {
		logger.info("### Inizio processo di recupero di tutti i ruoli presenti nel sistema ###");
		RoleResponse response = new RoleResponse();
		List<RoleDTO> roles = roleRepository.findAll().stream()
	            .map(roleMapper::toDTO)
	            .collect(Collectors.toList());

		response.setRoles(roles);
		
		logger.info("### Recupero di tutti i ruoli presenti nel sistema completato con successo ###");
		return response;
	}
}
