package com.efioco.ticketsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TicketDTO {

	private String id;
    private String customerId;
    private String description;
    private String title;
    private TicketStatusDTO status;
    private CategoryDTO category;
    private UserDTO creator;
    private UserDTO assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean removed;
    private List<TicketMessageDTO> messages;
    private TicketUrgencyDTO urgency;
    private String creatorId;
    
    public TicketDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TicketStatusDTO getStatus() {
		return status;
	}

	public void setStatus(TicketStatusDTO status) {
		this.status = status;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public UserDTO getCreator() {
		return creator;
	}

	public void setCreator(UserDTO creator) {
		this.creator = creator;
	}

	public UserDTO getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(UserDTO assignedTo) {
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
    
	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	public List<TicketMessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(List<TicketMessageDTO> messages) {
		this.messages = messages;
	}

	public TicketUrgencyDTO getUrgency() {
		return urgency;
	}

	public void setUrgency(TicketUrgencyDTO urgency) {
		this.urgency = urgency;
	}

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
	
}
