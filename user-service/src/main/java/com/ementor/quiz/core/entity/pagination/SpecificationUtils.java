/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.core.entity.pagination;

import com.ementor.quiz.core.entity.pagination.filter.FilterCriteria;
import com.ementor.quiz.core.entity.pagination.filter.FilterOperation;
import com.ementor.quiz.core.entity.pagination.filter.FilterStatement;
import com.ementor.quiz.core.entity.pagination.filter.SortDirection;
import com.ementor.quiz.core.exceptions.EmentorPersistenceRuntimeException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SpecificationUtils {
	private static final Logger log = LoggerFactory.getLogger(SpecificationUtils.class);

	private SpecificationUtils() {
		throw new AssertionError("Class " + SpecificationUtils.class + " cannot be instantiated!");
	}

	public static <E, V extends Comparable<V>> Predicate filter(Root<E> root,
			CriteriaQuery<?> query,
			CriteriaBuilder cb,
			String field,
			FilterOperation operation,
			final V value,
			Class<E> entityClass,
			Class<V> valueClass) {
		return (Predicate) ((Function) FilterStatement.createFunctionMap(entityClass, valueClass)
			.get(operation)).apply(FilterStatement.of(root, cb, field, value));
	}

	public static <E, V extends Comparable<V>> Predicate filter(Root<E> root,
			CriteriaQuery<?> query,
			CriteriaBuilder cb,
			String field,
			FilterOperation operation,
			final Collection<V> values,
			Class<E> entityClass,
			Class<V> valueClass) {
		return (Predicate) ((Function) FilterStatement.createFunctionMap(entityClass, valueClass)
			.get(operation)).apply(FilterStatement.of(root, cb, field, values));
	}

	public static <E, V extends Comparable<V>> Predicate filter(Root<E> root,
			CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder,
			FilterCriteria<V> criteria,
			Class<E> entityClass) {
		Map<FilterOperation, Function<FilterStatement<E, V>, Predicate>> createFunctionMap = FilterStatement
			.createFunctionMap(entityClass, criteria.getValueClass());
		return criteria.getValues() != null && !criteria.getValues()
			.isEmpty()
					? (Predicate) ((Function) createFunctionMap.get(criteria.getOperation()))
						.apply(FilterStatement.of(root, criteriaBuilder, criteria.getKey(), criteria.getValues()))
					: (Predicate) ((Function) createFunctionMap.get(criteria.getOperation()))
						.apply(FilterStatement.of(root, criteriaBuilder, criteria.getKey(), criteria.getValue()));
	}

	public static <E extends Serializable> Predicate createQuery(List<FilterCriteria<?>> filters,
			final Root<E> root,
			final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder,
			final Class<E> entityClass,
			final Boolean hasSorting) {
		return criteriaBuilder.and(createRestrictions(filters, root, query, criteriaBuilder, entityClass));
	}

	public static <E extends Serializable> Predicate createQuery(final PaginatedRequest request,
			final Root<E> root,
			final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder,
			final Class<E> entityClass,
			final Boolean hasSorting) {
		if (!request.isBinded()) {
			throw new EmentorPersistenceRuntimeException("Request not binded to main entity!");
		} else {
			Predicate predicate = criteriaBuilder
				.and(createRestrictions(request.getFilters(), root, query, criteriaBuilder, entityClass));
			if (hasSorting != null && hasSorting) {
				query.orderBy((List) request.getSorters()
					.stream()
					.map((sorter) -> {
						return SortDirection.ASC.equals(sorter.getDirection())
								? criteriaBuilder.asc(root.get(sorter.getKey()))
								: criteriaBuilder.desc(root.get(sorter.getKey()));
					})
					.collect(Collectors.toList()));
			}

			return predicate;
		}
	}

	public static <E extends Serializable> Predicate[] createRestrictions(List<FilterCriteria<?>> filters,
			Root<E> root,
			CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder,
			Class<E> entityClass) {
		return (Predicate[]) ((List) filters.stream()
			.map((criteria) -> {
				return filter(root, query, criteriaBuilder, criteria, entityClass);
			})
			.collect(Collectors.toList())).toArray(new Predicate[0]);
	}
}