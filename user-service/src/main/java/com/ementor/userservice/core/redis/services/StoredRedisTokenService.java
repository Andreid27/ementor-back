/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.redis.services;

import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.redis.repo.StoredRedisTokenRepo;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoredRedisTokenService {

	private static final Logger log = LoggerFactory.getLogger(StoredRedisTokenService.class);

	private final StoredRedisTokenRepo repo;

	public void buildAndSaveToken(UserDetails userDetails,
			String token) {
		StoredRedisToken storedRedisToken = StoredRedisToken.builder()
			.id(userDetails.getUsername())
			.token(token)
			.revoked(false)
			.userId(UUID.randomUUID()) // TODO "later to be added if needed"
			.build();
		saveStoredRedisToken(storedRedisToken);
	}

	private void saveStoredRedisToken(StoredRedisToken storedRedisToken) {
		try {
			repo.save(storedRedisToken);
		} catch (Exception e) {
			log.error("Eroare la salvarea StoredRedisToken in Redis");
			throw e;
		}
	}

	public Optional<StoredRedisToken> getStoredRedisToken(String userName) {
		return repo.findById(userName);
	}

}
