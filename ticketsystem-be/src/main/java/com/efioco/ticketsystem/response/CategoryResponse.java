package com.efioco.ticketsystem.response;

import java.util.List;

import com.efioco.ticketsystem.dto.CategoryDTO;

public class CategoryResponse {

    private CategoryDTO category;
	private List<CategoryDTO> categories;

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryDTO> categories) {
		this.categories = categories;
	}

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

}
