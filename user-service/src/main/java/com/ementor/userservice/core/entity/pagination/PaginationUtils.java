/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.entity.pagination;

import com.ementor.userservice.core.entity.pagination.filter.FilterCriteria;
import com.ementor.userservice.core.entity.pagination.filter.FilterOperation;
import java.util.List;
import java.util.UUID;

public class PaginationUtils {

	private PaginationUtils() {
	}

	private static FilterCriteria<String> filterCriteria(String key,
			List<String> values) {
		FilterCriteria<String> fc = new FilterCriteria<>();
		fc.setKey(key);
		fc.setOperation(FilterOperation.IN);
		fc.setValues(values);
		return fc;
	}

	public static FilterCriteria<UUID> filterCriteria(String key,
			UUID value) {
		FilterCriteria<UUID> fc = new FilterCriteria<>();
		fc.setKey(key);
		fc.setOperation(FilterOperation.EQUAL);
		fc.setValue(value);
		return fc;
	}

	public static void addFilterCriteria(PaginatedRequest request,
			String key,
			List<String> values) {
		request.getFilters()
			.add(filterCriteria(key, values));
	}

	public static void addFilterCriteria(PaginatedRequest request,
			String key,
			UUID value) {
		request.getFilters()
			.add(filterCriteria(key, value));
	}

	public static void removeFilterCriteria(PaginatedRequest request,
			String key) {
		request.getFilters()
			.removeIf(f -> f.getKey()
				.equals(key));
	}
}