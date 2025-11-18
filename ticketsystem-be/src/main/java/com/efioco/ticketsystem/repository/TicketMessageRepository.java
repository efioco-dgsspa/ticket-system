package com.efioco.ticketsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efioco.ticketsystem.entity.TicketMessageEntity;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessageEntity, UUID> {

}
