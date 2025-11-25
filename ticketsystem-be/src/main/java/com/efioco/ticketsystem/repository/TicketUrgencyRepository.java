package com.efioco.ticketsystem.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.efioco.ticketsystem.entity.TicketUrgencyEntity;

public interface TicketUrgencyRepository extends JpaRepository<TicketUrgencyEntity, UUID> {
    
	Optional<TicketUrgencyEntity> findByName(String name);

}
