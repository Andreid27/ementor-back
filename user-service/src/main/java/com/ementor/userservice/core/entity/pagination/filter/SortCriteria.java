/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.entity.pagination.filter;

public class SortCriteria {
	private String key;
	private SortDirection direction;

	public SortCriteria() {
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public SortDirection getDirection() {
		return this.direction;
	}

	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}

	public String toString() {
		return "SortCriteria [key=" + this.key + ", direction=" + this.direction + "]";
	}
}
