package com.efioco.ticketsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efioco.ticketsystem.entity.TicketTemplateEntity;

public interface TicketTemplateRepository extends JpaRepository<TicketTemplateEntity, UUID> {
    
	List<TicketTemplateEntity> findByCategoryId(UUID categoryId);

}
