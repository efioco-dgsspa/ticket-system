package com.efioco.ticketsystem.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permissions", 
		joinColumns = @JoinColumn(name = "role_id"), 
		inverseJoinColumns = @JoinColumn(name = "permission_id"))
	private Set<PermissionEntity> permissions;

	public RoleEntity() {
		
	}

	public RoleEntity(String name, Set<PermissionEntity> permissions) {
		this.name = name;
		this.permissions = permissions;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<PermissionEntity> permissions) {
		this.permissions = permissions;
	}
}
