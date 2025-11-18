package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.RoleDTO;

public class RoleResponse {

	private List<RoleDTO> roles;

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}
}
