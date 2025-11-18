package com.efioco.ticketsystem.service;

import java.util.Set;

import com.efioco.ticketsystem.entity.PermissionEntity;
import com.efioco.ticketsystem.entity.RoleEntity;
import com.efioco.ticketsystem.response.RoleResponse;

public interface RoleServiceInterface {

	RoleEntity findOrCreateRole(String name, Set<PermissionEntity> permissions);
	RoleResponse getAllRoles();

}
