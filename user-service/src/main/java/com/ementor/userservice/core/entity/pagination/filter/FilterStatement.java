/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.entity.pagination.filter;

import com.ementor.userservice.core.exceptions.EmentorPersistenceRuntimeException;
import jakarta.persistence.criteria.*;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FilterStatement<E, V extends Comparable<V>> {

	private static final Logger log = LoggerFactory.getLogger(FilterStatement.class);

	private Root<E> root;
	private CriteriaBuilder criteriaBuilder;
	private String field;
	private V value;
	private Collection<V> values;

	public FilterStatement(
			Root<E> root,
			CriteriaBuilder criteriaBuilder,
			String field,
			V value) {
		super();
		this.root = root;
		this.criteriaBuilder = criteriaBuilder;
		this.field = field;
		this.value = value;
	}

	public FilterStatement(
			Root<E> root,
			CriteriaBuilder criteriaBuilder,
			String field,
			Collection<V> values) {
		super();
		this.root = root;
		this.criteriaBuilder = criteriaBuilder;
		this.field = field;
		this.values = values;
	}

	public static <E, V extends Comparable<V>> FilterStatement<E, V> of(Root<E> root,
			CriteriaBuilder criteriaBuilder,
			String field,
			V value) {
		return new FilterStatement<>(root, criteriaBuilder, field, value);
	}

	public static <E, V extends Comparable<V>> FilterStatement<E, V> of(Root<E> root,
			CriteriaBuilder criteriaBuilder,
			String field,
			Collection<V> values) {
		return new FilterStatement<>(root, criteriaBuilder, field, values);
	}

	public static <E, V extends Comparable<V>> Map<FilterOperation, Function<FilterStatement<E, V>, Predicate>> createFunctionMap(
			Class<E> entityClass,
			Class<V> valueClass) {
		final Map<FilterOperation, Function<FilterStatement<E, V>, Predicate>> functions = new EnumMap<>(
				FilterOperation.class);

		functions.put(FilterOperation.EQUAL, statement -> statement.getCriteriaBuilder()
			.equal(treatStatementPath(statement), statement.getValue()));
		functions.put(FilterOperation.NOT_EQUAL, statement -> statement.getCriteriaBuilder()
			.notEqual(treatStatementPath(statement), statement.getValue()));
		functions.put(FilterOperation.IN, statement -> treatStatementPath(statement).in(statement.getValues()));
		functions.put(FilterOperation.NOT_IN, statement -> treatStatementPath(statement).in(statement.getValues())
			.not());

		functions.put(FilterOperation.IS_NOT_NULL, statement -> statement.getCriteriaBuilder()
			.isNotNull(statement.getPath()));
		functions.put(FilterOperation.IS_NULL, statement -> statement.getCriteriaBuilder()
			.isNull(statement.getPath()));

		functions.put(FilterOperation.GREATER, statement -> statement.getCriteriaBuilder()
			.greaterThan(statement.getPath(), statement.getValue()));
		functions.put(FilterOperation.GREATER_OR_EQUAL, statement -> statement.getCriteriaBuilder()
			.greaterThanOrEqualTo(statement.getPath(), statement.getValue()));
		functions.put(FilterOperation.LESS, statement -> statement.getCriteriaBuilder()
			.lessThan(statement.getPath(), statement.getValue()));
		functions.put(FilterOperation.LESS_OR_EQUAL, statement -> statement.getCriteriaBuilder()
			.lessThanOrEqualTo(statement.getPath(), statement.getValue()));

		functions.put(FilterOperation.LIKE, statement -> {
			try {
				@SuppressWarnings("unchecked")
				final Expression<String> newStatement = (Expression<String>) statement.getPath();
				return statement.getCriteriaBuilder()
					.like(statement.getCriteriaBuilder()
						.lower(newStatement),
							("%" + ((String) statement.getValue()).trim()
								.replaceAll("\\s+", "%") + "%").toLowerCase());
			} catch (Exception e) {
				throw new EmentorPersistenceRuntimeException("Invalid input type for " + FilterOperation.LIKE + " clause.",
						e);
			}
		});

		functions.put(FilterOperation.BEGINS_WITH, statement -> {
			try {
				@SuppressWarnings("unchecked")
				final Expression<String> newStatement = (Expression<String>) statement.getPath();
				return statement.getCriteriaBuilder()
					.like(statement.getCriteriaBuilder()
						.lower(newStatement), (((String) statement.getValue()).trim() + "%").toLowerCase());
			} catch (Exception e) {
				throw new EmentorPersistenceRuntimeException(
						"Invalid input type for " + FilterOperation.BEGINS_WITH + " clause.", e);
			}
		});

		functions.put(FilterOperation.ENDS_WITH, statement -> {
			try {
				@SuppressWarnings("unchecked")
				final Expression<String> newStatement = (Expression<String>) statement.getPath();
				final String lowerCase = ("%" + ((String) statement.getValue()).trim()).toLowerCase();
				return statement.getCriteriaBuilder()
					.like(statement.getCriteriaBuilder()
						.lower(newStatement), lowerCase);
			} catch (Exception e) {
				throw new EmentorPersistenceRuntimeException(
						"Invalid input type for " + FilterOperation.ENDS_WITH + " clause.", e);
			}
		});

		return functions;
	}

	private static <E, V extends Comparable<V>> Expression<?> treatStatementPath(FilterStatement<E, V> statement) {
		if (statement.getValue() != null) {
			log.debug("Check if path {} is for String, because the value is {}", statement.getPath(), statement.getValue()
				.getClass());
			return statement.getValue()
				.getClass()
				.equals(String.class)
						? statement.getPath()
							.as(String.class)
						: statement.getPath();

		}
		if (statement.getValues() != null && !statement.getValues()
			.isEmpty()) {
			final V firstValue = statement.getValues()
				.iterator()
				.next();
			log.debug("Check if path {} is for String, because the value is {}", statement.getPath(), firstValue.getClass());
			return firstValue.getClass()
				.equals(String.class)
						? statement.getPath()
							.as(String.class)
						: statement.getPath();
		}
		return statement.getPath();
	}

	public Root<E> getRoot() {
		return root;
	}

	public void setRoot(Root<E> root) {
		this.root = root;
	}

	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	public void setCriteriaBuilder(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Collection<V> getValues() {
		return values;
	}

	public void setValues(Collection<V> values) {
		this.values = values;
	}

	public Path<V> getPath() {
		return this.root.get(this.field);
	}
}
