package com.efioco.ticketsystem.dto;

import java.util.Set;

public class RoleDTO {
	
	private String id;
	private String name;
	private Set<PermissionDTO> permissions;
	
	public RoleDTO() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<PermissionDTO> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<PermissionDTO> permissions) {
		this.permissions = permissions;
	}
	
}
