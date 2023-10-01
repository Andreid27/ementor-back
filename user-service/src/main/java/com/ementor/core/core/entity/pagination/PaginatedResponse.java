/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.entity.pagination;

import com.ementor.core.core.entity.pagination.filter.FilterOption;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public class PaginatedResponse<T> {
	private List<T> data;
	private Integer page;
	private Integer totalPages;
	private Long totalCount;
	private List<FilterOption<?>> filterOptions;
	private PaginatedRequest currentRequest;

	public PaginatedResponse() {
	}

	public List<T> getData() {
		return this.data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Integer getPage() {
		return this.page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotalPages() {
		return this.totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public List<FilterOption<?>> getFilterOptions() {
		return this.filterOptions;
	}

	public void setFilterOptions(List<FilterOption<?>> filterOptions) {
		this.filterOptions = filterOptions;
	}

	public PaginatedRequest getCurrentRequest() {
		return this.currentRequest;
	}

	public void setCurrentRequest(PaginatedRequest currentRequest) {
		this.currentRequest = currentRequest;
		this.currentRequest.unbind();
	}
}
