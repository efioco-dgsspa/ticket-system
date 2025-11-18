package com.efioco.ticketsystem.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class TicketEntity {
	
	@Id
	@GeneratedValue
	@Column(nullable = false, updatable = false)
    private UUID id;
	
	@Column(name = "customer_id", unique = true, nullable = false)
    private String customerId;  // ID pubblico visibile al cliente

    private String title;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TicketStatusEntity status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne
    @JoinColumn(name = "assigned_to_id")
    private UserEntity assignedTo;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private boolean removed = false;  // ✅ cancellazione logica
    
    // Relazione 1-N con messaggi del ticket
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketMessageEntity> messages = new ArrayList<>();
    
    public TicketEntity() {
		
	}

	public TicketEntity(UUID id, String customerId, String title, TicketStatusEntity status, CategoryEntity category,
			UserEntity creator, UserEntity assignedTo, LocalDateTime createdAt, LocalDateTime updatedAt,
			boolean removed, List<TicketMessageEntity> messages) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.title = title;
		this.status = status;
		this.category = category;
		this.creator = creator;
		this.assignedTo = assignedTo;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.removed = removed;
		this.messages = messages;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TicketStatusEntity getStatus() {
		return status;
	}

	public void setStatus(TicketStatusEntity status) {
		this.status = status;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public UserEntity getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}

	public UserEntity getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(UserEntity assignedTo) {
		this.assignedTo = assignedTo;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean getRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public List<TicketMessageEntity> getMessages() {
		return messages;
	}

	public void setMessages(List<TicketMessageEntity> messages) {
		this.messages = messages;
	}
    
}
