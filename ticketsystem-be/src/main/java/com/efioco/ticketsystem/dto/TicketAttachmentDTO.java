package com.efioco.ticketsystem.dto;

public class TicketAttachmentDTO {

	private String id;
    private String filename;
    private String contentType;
    private byte[] content;
    private TicketMessageDTO message;
    
    public TicketAttachmentDTO() {
        
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public TicketMessageDTO getMessage() {
		return message;
	}

	public void setMessage(TicketMessageDTO message) {
		this.message = message;
	}
    
}
