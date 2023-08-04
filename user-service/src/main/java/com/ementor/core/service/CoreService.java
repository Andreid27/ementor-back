/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.service;

import com.ementor.core.repo.UsersRepo;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoreService {
	private final UsersRepo repository;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public String localDateTimeLogger() {
		return String.valueOf(OffsetDateTime.now());
	}

}
