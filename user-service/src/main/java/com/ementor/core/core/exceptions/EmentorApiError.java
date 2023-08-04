/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Arrays;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(value = {"cause", "localizedMessage", "suppressed", "stackTrace"})
public class EmentorApiError extends RuntimeException {
	private static final long serialVersionUID = 1613264748624991373L;

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Collection<Object> rejected;

	private Integer httpStatusCode;

	public EmentorApiError(
			String message) {
		super(message);
		this.httpStatusCode = 500;
	}

	public EmentorApiError(
			String message,
			Throwable throwable) {
		super(message, throwable);
		this.httpStatusCode = 500;
	}

	public EmentorApiError(
			String message,
			Integer httpStatusCode) {
		super(message);
		this.httpStatusCode = httpStatusCode;
	}

	public EmentorApiError(
			String message,
			Object... rejected) {
		super(message);
		this.rejected = Arrays.asList(rejected);
		this.httpStatusCode = 500;
	}

	public EmentorApiError(
			String message,
			Throwable throwable,
			Object... rejected) {
		super(message, throwable);
		this.rejected = Arrays.asList(rejected);
		this.httpStatusCode = 500;
	}

	public EmentorApiError(
			String message,
			Integer httpStatusCode,
			Object... rejected) {
		super(message);
		this.rejected = Arrays.asList(rejected);
		this.httpStatusCode = httpStatusCode;
	}

	public EmentorApiError(
			String message,
			Integer httpStatusCode,
			Throwable throwable,
			Object... rejected) {
		super(message, throwable);
		this.rejected = Arrays.asList(rejected);
		this.httpStatusCode = httpStatusCode;
		log.error("Rejected: {}", this.rejected);
	}

	public Collection<Object> getRejected() {
		return rejected;
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public static EmentorApiError badRequest(final String message) {
		return new EmentorApiError(message, 400);
	}

	public static EmentorApiError unauthorized(final String message) {
		return new EmentorApiError(message, 401);
	}

	public static EmentorApiError forbidden(final String message) {
		return new EmentorApiError(message, 403);
	}
}
