package com.efioco.ticketsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.efioco.ticketsystem.dto.CategoryDTO;
import com.efioco.ticketsystem.mapper.CategoryMapper;
import com.efioco.ticketsystem.repository.CategoryRepository;
import com.efioco.ticketsystem.response.CategoryResponse;

@Service
public class CategoryService implements CategoryServiceInterface {

	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
    public CategoryResponse getAllCategories() {
		logger.info("### Inizio processo di recupero di tutte le categorie presenti nel sistema ###");
		CategoryResponse response = new CategoryResponse();
		List<CategoryDTO> categories = categoryMapper.toDTOList(categoryRepository.findAll());

		response.setCategories(categories);
		
		logger.info("### Recupero di tutte le categorie presenti nel sistema completato con successo ###");
		return response;
	}
}
