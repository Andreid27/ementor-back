/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity.pagination.filter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"valueClass"})
public class FilterCriteria<V extends Comparable<V>> {
	private String key;
	private FilterOperation operation;
	private V value;
	private Collection<V> values;
	private Class<V> valueClass;

	public FilterCriteria() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public FilterOperation getOperation() {
		return this.operation;
	}

	public void setOperation(FilterOperation operation) {
		this.operation = operation;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Collection<V> getValues() {
		return this.values;
	}

	public void setValues(Collection<V> values) {
		this.values = values;
	}

	public Class<V> getValueClass() {
		return this.valueClass;
	}

	public void setValueClass(Class<V> valueClass) {
		this.valueClass = valueClass;
	}

	public String toString() {
		return "FilterCriteria [key=" + this.key + ", operation=" + this.operation + ", value=" + this.value + ", values="
				+ this.values + ", valueClass=" + this.valueClass + "]";
	}
}
