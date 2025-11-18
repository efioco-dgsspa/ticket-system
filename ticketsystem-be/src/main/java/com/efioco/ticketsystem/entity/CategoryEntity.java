package com.efioco.ticketsystem.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "categories")
public class CategoryEntity {

	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String code; // esempio: "NET", "HW", "SW"

	@Column(nullable = false)
	private String name; // esempio: "Network", "Hardware", "Software"

	@Column
	private String description;

	public CategoryEntity() {
	}

	public CategoryEntity(String code, String name, String description) {
		this.code = code;
		this.name = name;
		this.description = description;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
