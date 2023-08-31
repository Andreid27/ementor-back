/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity.pagination;

import com.ementor.profile.service.core.entity.pagination.filter.FilterCriteria;
import com.ementor.profile.service.core.entity.pagination.filter.FilterCriteriaUtils;
import com.ementor.profile.service.core.entity.pagination.filter.FilterOperation;
import com.ementor.profile.service.core.entity.pagination.filter.SortCriteria;
import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"binded"})
public class PaginatedRequest implements Serializable {
	private static final long serialVersionUID = -5567927611490065670L;
	private List<FilterCriteria<?>> filters;
	private List<SortCriteria> sorters;
	private Integer page;
	private Integer pageSize;
	private Class<?> entityClass;

	public PaginatedRequest() {
		this.page = 0;
		this.pageSize = 20;
	}

	public PaginatedRequest(
			Integer page,
			Integer pageSize) {
		this.page = page;
		this.pageSize = pageSize > 100 ? 100 : pageSize;
	}

	public List<FilterCriteria<?>> getFilters() {
		return this.filters;
	}

	public void setFilters(List<FilterCriteria<?>> filters) {
		this.filters = filters;
	}

	public List<SortCriteria> getSorters() {
		return this.sorters;
	}

	public void setSorters(List<SortCriteria> sorters) {
		this.sorters = sorters;
	}

	public Integer getPage() {
		return this.page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize > 100 ? 100 : pageSize;
	}

	public <E extends Serializable> PaginatedRequest bind(Class<E> entityClass) {
		this.filters = (List) this.filters.stream()
			.map((f) -> {
				try {
					return FilterCriteriaUtils.convert(f, entityClass.getDeclaredField(f.getKey())
						.getType());
				} catch (SecurityException | NoSuchFieldException var3) {
					throw new EmentorApiError("Field " + f.getKey() + " does not exist!", var3);
				} catch (Exception var4) {
					throw new EmentorApiError("Generic error!", var4);
				}
			})
			.collect(Collectors.toList());
		this.entityClass = entityClass;
		return this;
	}

	public Boolean isBinded() {
		return this.entityClass != null;
	}

	public void unbind() {
		this.entityClass = null;
	}

	public <T extends Comparable<T>> void addKey(String key,
			T value,
			FilterOperation filterOperation,
			Class<T> clazz) {
		FilterCriteria<T> typeCodeFilter = new FilterCriteria();
		typeCodeFilter.setKey(key);
		typeCodeFilter.setOperation(filterOperation);
		typeCodeFilter.setValue(value);
		typeCodeFilter.setValueClass(clazz);
		if (this.getFilters() != null && !this.getFilters()
			.isEmpty()) {
			List<FilterCriteria<?>> newFilters = (List) this.getFilters()
				.stream()
				.filter((f) -> {
					return !f.getKey()
						.equals(key);
				})
				.collect(Collectors.toList());
			newFilters.add(typeCodeFilter);
			this.setFilters(newFilters);
		} else {
			List<FilterCriteria<?>> newFilters = new ArrayList();
			newFilters.add(typeCodeFilter);
			this.setFilters(newFilters);
		}

	}

	public String toString() {
		return "PaginatedRequest [filters=" + this.filters + ", sorters=" + this.sorters + ", page=" + this.page
				+ ", pageSize=" + this.pageSize + ", entityClass=" + this.entityClass + "]";
	}
}