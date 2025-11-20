	package com.efioco.ticketsystem.specification;
	
	import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.efioco.ticketsystem.dto.UserDTO;
import com.efioco.ticketsystem.entity.RoleEntity;
import com.efioco.ticketsystem.entity.UserEntity;
import com.efioco.ticketsystem.request.UserRequest;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
	
	public class UserSpecification implements Specification<UserEntity> {
	
		private static final long serialVersionUID = 1L;
		private final UserDTO filter;
	
		public UserSpecification(UserRequest request) {
			this.filter = request.getUser();
		}
	
		@Override
		public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
			List<Predicate> predicati = new ArrayList<>();
	
			if (isFilterEmpty(filter)) {
				return cb.isTrue(cb.literal(true));
			}
	
			// username like
			if (StringUtils.isNotBlank(filter.getUsername())) {
				predicati.add(cb.like(cb.lower(root.get("username")), "%" + filter.getUsername().toLowerCase() + "%"));
			}
	
			// title like
			if (StringUtils.isNotBlank(filter.getEmail())) {
				predicati.add(cb.like(cb.lower(root.get("email")), "%" + filter.getEmail().toLowerCase() + "%"));
			}
	
			// role
			if (!CollectionUtils.isEmpty(filter.getRoles())) {
				List<UUID> roleIds = filter.getRoles().stream()
			            .map(r -> UUID.fromString(r.getId()))
			            .toList();

			    Join<UserEntity, RoleEntity> rolesJoin = root.join("roles");

			    predicati.add(rolesJoin.get("id").in(roleIds));
			    query.distinct(true);
			}
	
			// removed flag
			if (filter.getActive() != null) {
				predicati.add(cb.equal(root.get("active"), filter.getActive()));
			}
	
			return cb.and(predicati.toArray(new Predicate[0]));
		}
	
		private boolean isFilterEmpty(UserDTO filter) {
	
			if (StringUtils.isBlank(filter.getUsername()) && StringUtils.isBlank(filter.getEmail())
					&& CollectionUtils.isEmpty(filter.getRoles())
					&& filter.getActive() == null) {
	
				return true;
			} else {
				return false;
			}
		}
	}
