/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.core.exceptions;

public class EmentorPersistenceRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1695316120805073716L;

	public EmentorPersistenceRuntimeException(
			String message) {
		super(message);
	}

	public EmentorPersistenceRuntimeException(
			String message,
			Throwable throwable) {
		super(message, throwable);
	}
}