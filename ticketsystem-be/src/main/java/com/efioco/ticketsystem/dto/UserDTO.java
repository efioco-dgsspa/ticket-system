package com.efioco.ticketsystem.dto;

import java.util.Set;

public class UserDTO {
	
	private String id;
	private String username;
    private String email;
    private String password;
    private String passwordCorrente;
    private String nuovaPassword;
    private Set<RoleDTO> roles;
	private Boolean active;
    
    public UserDTO() {
    	
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordCorrente() {
		return passwordCorrente;
	}

	public void setPasswordCorrente(String passwordCorrente) {
		this.passwordCorrente = passwordCorrente;
	}

	public String getNuovaPassword() {
		return nuovaPassword;
	}

	public void setNuovaPassword(String nuovaPassword) {
		this.nuovaPassword = nuovaPassword;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleDTO> roles) {
		this.roles = roles;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
    
}
