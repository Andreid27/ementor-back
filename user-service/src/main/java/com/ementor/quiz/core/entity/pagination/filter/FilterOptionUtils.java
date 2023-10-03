/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.core.entity.pagination.filter;

import com.ementor.quiz.core.entity.pagination.PaginatedRequest;
import com.ementor.quiz.core.entity.pagination.SpecificationUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterOptionUtils {

	private static final Logger log = LoggerFactory.getLogger(FilterOptionUtils.class);

	private FilterOptionUtils() {
		throw new AssertionError("Class " + FilterOptionUtils.class + " cannot be instantiated!");
	}

	private enum AggregateType {
		MIN, MAX
	}

	public static <E extends Serializable> FilterOption<?> createFilterOption(final EntityManager em,
			final PaginatedRequest request,
			final Class<E> entityClass,
			final String field,
			FilterType filterType) {
		try {

			final Class<?> fieldType = entityClass.getDeclaredField(field)
				.getType();
			if ((String.class.equals(fieldType) || fieldType.isEnum()) && FilterType.TEXT_OPTIONS.equals(filterType)) {
				return new FilterOption<>(field, filterType, getDistinctOptions(em, request, entityClass, field));
			}

			if (Boolean.class.equals(fieldType) && FilterType.BOOLEAN_OPTIONS.equals(filterType)) {
				return new FilterOption<>(field, filterType, getDistinctOptions(em, request, entityClass, field));
			}

			if ((Integer.class.equals(fieldType) || Long.class.equals(fieldType) || BigDecimal.class.equals(fieldType))
					&& FilterType.NUMBER.equals(filterType)) {
				return new FilterOption<>(field, filterType, getDistinctOptions(em, request, entityClass, field));
			}

			if ((Integer.class.equals(fieldType) || Long.class.equals(fieldType) || BigDecimal.class.equals(fieldType))
					&& FilterType.NUMBER_RANGE.equals(filterType)) {
				return new FilterOption<>(field, filterType,
						getAggregateValue(em, request, entityClass, field, AggregateType.MIN),
						getAggregateValue(em, request, entityClass, field, AggregateType.MAX));
			}

			if ((String.class.equals(fieldType) || UUID.class.equals(fieldType))
					&& (FilterType.TEXT.equals(filterType) || FilterType.TEXT_CONTENT.equals(filterType))) {
				return new FilterOption<>(field, filterType);
			}

			if ((isDateTime(fieldType) || isDate(fieldType)) && FilterType.DATE_TIME_RANGE.equals(filterType)) {
				return new FilterOption<>(field, filterType,
						getAggregateValue(em, request, entityClass, field, AggregateType.MIN),
						getAggregateValue(em, request, entityClass, field, AggregateType.MAX));
			}

		} catch (NoSuchFieldException | SecurityException e) {
			log.error("Field " + field + " is not accessible for class " + entityClass.toString(), e);
		}
		return null;
	}

	private static boolean isDateTime(final Class<?> fieldType) {
		return ZonedDateTime.class.equals(fieldType) || OffsetDateTime.class.equals(fieldType)
				|| ChronoLocalDateTime.class.equals(fieldType) || LocalDateTime.class.equals(fieldType)
				|| Date.class.equals(fieldType);
	}

	private static boolean isDate(final Class<?> fieldType) {
		return ChronoLocalDate.class.equals(fieldType) || LocalDate.class.equals(fieldType) || Date.class.equals(fieldType);
	}

	private static <E extends Serializable> List<?> getDistinctOptions(final EntityManager em,
			final PaginatedRequest request,
			final Class<E> eC,
			final String f) throws NoSuchFieldException,
			SecurityException {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<?> cq = cb.createQuery(eC.getDeclaredField(f)
			.getType());
		Root<E> root = cq.from(eC);
		cq.select(root.get(f))
			.where(cb.and(toPredicate(request, root, cq, cb, eC, f)))
			.distinct(true)
			.orderBy(cb.asc(root.get(f)));
		TypedQuery<?> createQuery = em.createQuery(cq);
		return createQuery.getResultList();
	}

	/*
	 * FIXME: If we remove the (Selection) cast then we get error:
	 * "Criteria builder inference variable N has incompatible upper bounds capture"
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	private static <E extends Serializable> Object getAggregateValue(final EntityManager em,
			final PaginatedRequest request,
			final Class<E> eC,
			final String f,
			AggregateType aggT) throws NoSuchFieldException,
			SecurityException {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<?> cq = cb.createQuery(eC.getDeclaredField(f)
			.getType());
		Root<E> root = cq.from(eC);
		if (AggregateType.MIN.equals(aggT)) {
			cq.select((Selection) cb.min(root.get(f)))
				.where(cb.and(toPredicate(request, root, cq, cb, eC, f)));
		} else {
			cq.select((Selection) cb.max(root.get(f)))
				.where(cb.and(toPredicate(request, root, cq, cb, eC, f)));
		}
		TypedQuery<?> createQuery = em.createQuery(cq);
		return createQuery.getSingleResult();
	}

	private static <E extends Serializable> List<FilterCriteria<?>> getCompiledFiltersCriterias(
			List<FilterCriteria<?>> currentFilters,
			final String f) {
		List<FilterCriteria<?>> filters = new ArrayList<>();
		for (FilterCriteria<?> criteria : currentFilters) {
			if (criteria.getKey()
				.equals(f)) {
				break;
			} else {
				filters.add(criteria);
			}
		}
		return filters;
	}

	public static List<FilterCriteria<?>> getCompiledFiltersCriterias(PaginatedRequest request,
			String field) {
		return getCompiledFiltersCriterias(request.getFilters(), field);
	}

	public static <E extends Serializable> List<FilterOption<?>> createFilterOptions(final EntityManager em,
			final PaginatedRequest request,
			final Class<E> entityClass,
			FilterGroup... groups) {
		return Stream.of(groups)
			.map(g -> createFilterOption(em, request, entityClass, g.getKey(), g.getType()))
			.collect(Collectors.toList());
	}

	public static <E extends Serializable> Predicate toPredicate(final PaginatedRequest request,
			final Root<E> root,
			final CriteriaQuery<?> query,
			final CriteriaBuilder criteriaBuilder,
			final Class<E> entityClass,
			final String field) {
		return SpecificationUtils.createQuery(getCompiledFiltersCriterias(request, field), root, query, criteriaBuilder,
				entityClass, false);
	}

}
