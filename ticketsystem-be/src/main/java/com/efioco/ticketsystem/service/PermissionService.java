package com.efioco.ticketsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.entity.PermissionEntity;
import com.efioco.ticketsystem.repository.PermissionRepository;

@Service
public class PermissionService implements PermissionServiceInterface {
	
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public PermissionEntity findOrCreatePermission(String name) {
		return permissionRepository
				.findByName(name)
				.orElseGet(() -> permissionRepository.save(new PermissionEntity(name)));
	}
}
