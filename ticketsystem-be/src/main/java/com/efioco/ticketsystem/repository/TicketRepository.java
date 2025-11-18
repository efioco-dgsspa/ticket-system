package com.efioco.ticketsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efioco.ticketsystem.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

}
