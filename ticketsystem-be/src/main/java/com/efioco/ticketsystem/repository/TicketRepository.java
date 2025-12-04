package com.efioco.ticketsystem.repository;

import java.util.Optional;
import java.util.UUID;

import com.efioco.ticketsystem.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efioco.ticketsystem.entity.TicketEntity;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    Page<TicketEntity> findAll(Specification<TicketEntity> specification, Pageable pageable);

}
