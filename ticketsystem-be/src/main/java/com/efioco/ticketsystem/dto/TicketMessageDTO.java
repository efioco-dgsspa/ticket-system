package com.efioco.ticketsystem.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TicketMessageDTO {

	private String id;
    private TicketDTO ticket;
    private UserDTO author;
    private String description;
    private List<TicketAttachmentDTO> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public TicketMessageDTO() {
    
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TicketDTO getTicket() {
		return ticket;
	}

	public void setTicket(TicketDTO ticket) {
		this.ticket = ticket;
	}

	public UserDTO getAuthor() {
		return author;
	}

	public void setAuthor(UserDTO author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<TicketAttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TicketAttachmentDTO> attachments) {
		this.attachments = attachments;
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
    
}
