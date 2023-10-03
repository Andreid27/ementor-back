/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.core.entity.pagination.filter;

public class FilterGroup {
	private String key;
	private FilterType type;

	public FilterGroup(
			String key,
			FilterType type) {
		this.key = key;
		this.type = type;
	}

	public String getKey() {
		return this.key;
	}

	public FilterType getType() {
		return this.type;
	}
}