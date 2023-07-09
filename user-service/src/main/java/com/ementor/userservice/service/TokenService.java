/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.redis.repo.StoredRedisTokenRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

	private final StoredRedisTokenRepo dao;

	public void storeRedisToken(String id,
			String token) {
		StoredRedisToken storedRedisToken = new StoredRedisToken();
		storedRedisToken.setToken(token);
		storedRedisToken.setId(id);
		dao.save(storedRedisToken);
	}

	public List<StoredRedisToken> getAllTokens() {
		Iterable<StoredRedisToken> tokens = dao.findAll();
		List<StoredRedisToken> tokenList = new ArrayList<>();
		tokens.forEach(tokenList::add);
		return tokenList;
	}

	public StoredRedisToken getToken(String token) {
		Optional<StoredRedisToken> redisToken = dao.findByToken(token);

		if (redisToken.isEmpty()) {
			return new StoredRedisToken();
		}
		return redisToken.get();
	}
}
