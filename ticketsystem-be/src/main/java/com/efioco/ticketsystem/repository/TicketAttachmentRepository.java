package com.efioco.ticketsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.efioco.ticketsystem.entity.TicketAttachmentEntity;

@Repository
public interface TicketAttachmentRepository extends JpaRepository<TicketAttachmentEntity, UUID> {

}
