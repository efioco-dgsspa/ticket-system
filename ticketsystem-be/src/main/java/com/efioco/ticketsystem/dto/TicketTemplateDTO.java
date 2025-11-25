package com.efioco.ticketsystem.dto;

public class TicketTemplateDTO {

	private String id;
    private CategoryDTO category;
    private String name;
    private String templateText;

    public TicketTemplateDTO() {
    	
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
    
}
