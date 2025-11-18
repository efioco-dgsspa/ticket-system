package com.efioco.ticketsystem.specification;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.efioco.ticketsystem.dto.TicketDTO;
import com.efioco.ticketsystem.entity.TicketEntity;
import com.efioco.ticketsystem.request.TicketRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class TicketSpecification implements Specification<TicketEntity> {

	private static final long serialVersionUID = 1L;
	private final TicketDTO filter;

	public TicketSpecification(TicketRequest request) {
		this.filter = request.getTicket();
	}

	@Override
	public Predicate toPredicate(Root<TicketEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicati = new ArrayList<>();
		
		if (!isFilterPresent(filter)) {
	        return cb.isTrue(cb.literal(true));
	    }

		// customerId
		if (StringUtils.isNotBlank(filter.getCustomerId())) {
			predicati.add(cb.equal(root.get("description"), filter.getCustomerId()));
		}

		// title like
		if (StringUtils.isNotBlank(filter.getTitle())) {
			predicati.add(cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
		}

		// status
		if (filter.getStatus() != null) {
			predicati.add(cb.equal(root.get("status").get("id"), filter.getStatus()));
		}

		// category
		if (filter.getCategory() != null) {
			predicati.add(cb.equal(root.get("category").get("id"), filter.getCategory()));
		}

		// creator
		if (filter.getCreator() != null) {
			predicati.add(cb.equal(root.get("creator").get("id"), filter.getCreator()));
		}

		// assignedTo
		if (filter.getAssignedTo() != null) {
			predicati.add(cb.equal(root.get("assignedTo").get("id"), filter.getAssignedTo()));
		}

		// date range
		if (filter.getCreatedAt() != null) {
			predicati.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedAt()));
		}
		if (filter.getUpdatedAt() != null) {
			predicati.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getUpdatedAt()));
		}

		// removed flag
		if (BooleanUtils.isTrue(filter.getRemoved())) {
			predicati.add(cb.isFalse(root.get("removed")));
		}

		return cb.and(predicati.toArray(new Predicate[0]));
	}

	private boolean isFilterPresent(TicketDTO filter) {
		
		if (StringUtils.isBlank(filter.getCustomerId()) && StringUtils.isBlank(filter.getTitle())
				&& filter.getStatus() == null && filter.getCategory() == null && filter.getCreator() == null 
				&& filter.getAssignedTo() == null && filter.getCreatedAt() == null && filter.getUpdatedAt() == null
				&& filter.getRemoved() == null) {
			
			return true;
		}
		else {
			return false;
		}
	}
	
}
