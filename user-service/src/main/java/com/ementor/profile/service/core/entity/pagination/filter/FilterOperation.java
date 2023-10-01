/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity.pagination.filter;

public enum FilterOperation {
	CUSTOM, EQUAL, NOT_EQUAL, GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL, LIKE, BEGINS_WITH, ENDS_WITH, IN, NOT_IN, IS_NULL, IS_NOT_NULL;

	private FilterOperation() {
	}
}