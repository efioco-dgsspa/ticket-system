package com.efioco.ticketsystem.service;

import com.efioco.ticketsystem.exceptions.CategoryServiceException;
import com.efioco.ticketsystem.response.CategoryResponse;

import java.util.UUID;

public interface CategoryServiceInterface {

	CategoryResponse getAllCategories();

    CategoryResponse getCategoryById(UUID id) throws CategoryServiceException;

}
