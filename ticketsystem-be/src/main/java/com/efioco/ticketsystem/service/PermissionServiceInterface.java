package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.entity.PermissionEntity;

public interface PermissionServiceInterface {

	PermissionEntity findOrCreatePermission(String name);

}
