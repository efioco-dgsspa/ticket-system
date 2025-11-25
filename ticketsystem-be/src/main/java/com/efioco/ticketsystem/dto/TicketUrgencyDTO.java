package com.efioco.ticketsystem.dto;

public class TicketUrgencyDTO {

	private String id;
    private String name;
    private String description;
    private int priority;

    public TicketUrgencyDTO() {
    	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
    
}
