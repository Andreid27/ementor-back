/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.entity.pagination;

import com.ementor.core.core.entity.pagination.filter.FilterCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PaginatedRequestSpecificationUtils {
	private static final Logger log = LoggerFactory.getLogger(PaginatedRequestSpecificationUtils.class);

	private PaginatedRequestSpecificationUtils() {
		throw new AssertionError("Class " + PaginatedRequestSpecificationUtils.class + " cannot be instantiated!");
	}

	public static <E extends Serializable> List<FilterCriteria<?>> getCompiledFiltersCriterias(
			final PaginatedRequestSpecification<E> spec,
			final String f) {
		PaginatedRequest request = spec.getRequest();
		List<FilterCriteria<?>> filters = new ArrayList();
		Iterator var4 = request.getFilters()
			.iterator();

		while (var4.hasNext()) {
			FilterCriteria<?> criteria = (FilterCriteria) var4.next();
			if (criteria.getKey()
				.equals(f)) {
				break;
			}

			filters.add(criteria);
		}

		return filters;
	}

	public static <E extends Serializable> PaginatedRequestSpecification<E> genericSpecification(
			final PaginatedRequest paginatedRequest,
			final Boolean enableSpecSorting,
			final Class<E> entityClass) {
		return new PaginatedRequestSpecification<E>() {
			private static final long serialVersionUID = 7505752742937013020L;
			private PaginatedRequest request = paginatedRequest;
			private Boolean hasSorting = enableSpecSorting;

			public Predicate toPredicate(Root<E> root,
					CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				return SpecificationUtils.createQuery(this.request, root, query, criteriaBuilder, entityClass,
						this.hasSorting);
			}

			public PaginatedRequest getRequest() {
				return this.request;
			}

			public void enableSorting() {
				this.hasSorting = true;
			}

			public void disableSorting() {
				this.hasSorting = false;
			}

			public Boolean hasSorting() {
				return this.hasSorting;
			}
		};
	}
}