/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity.pagination.filter;

public enum FilterType {
	DATE, DATE_RANGE, DATE_TIME_RANGE, NUMBER, NUMBER_RANGE, NUMBER_OPTIONS, TEXT, TEXT_CONTENT, TEXT_OPTIONS, BOOLEAN_OPTIONS;

	private FilterType() {
	}
}