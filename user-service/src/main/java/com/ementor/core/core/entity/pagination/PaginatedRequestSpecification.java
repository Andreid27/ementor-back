/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.entity.pagination;

import org.springframework.data.jpa.domain.Specification;

public interface PaginatedRequestSpecification<T> extends Specification<T> {
	PaginatedRequest getRequest();

	void enableSorting();

	void disableSorting();

	default Boolean hasSorting() {
		return false;
	}
}