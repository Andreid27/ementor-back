/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.entity.pagination.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class FilterOption<T> {
	private String key;
	private List<T> options;
	private T min;
	private T max;
	private FilterType filterType;

	public FilterOption() {
	}

	public FilterOption(
			String key,
			FilterType filterType) {
		this.key = key;
		this.filterType = filterType;
	}

	public FilterOption(
			String key,
			FilterType filterType,
			List<T> options) {
		this.key = key;
		this.filterType = filterType;
		this.options = options;
	}

	public FilterOption(
			String key,
			FilterType filterType,
			T min,
			T max) {
		this.key = key;
		this.filterType = filterType;
		this.min = min;
		this.max = max;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<T> getOptions() {
		return this.options;
	}

	public void setOptions(List<T> options) {
		this.options = options;
	}

	public T getMin() {
		return this.min;
	}

	public void setMin(T min) {
		this.min = min;
	}

	public T getMax() {
		return this.max;
	}

	public void setMax(T max) {
		this.max = max;
	}

	public FilterType getFilterType() {
		return this.filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
	}

	public String toString() {
		return "FilterOption [key=" + this.key + ", options=" + this.options + ", min=" + this.min + ", max=" + this.max
				+ ", filterType=" + this.filterType + "]";
	}
}
