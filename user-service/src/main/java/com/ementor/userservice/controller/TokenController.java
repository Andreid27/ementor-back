/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.controller;

import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.service.SecurityService;
import com.ementor.userservice.service.TokenService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/redisTokens")
@RequiredArgsConstructor
public class TokenController {

	private final TokenService tokenService;

	private final SecurityService securityService;

	@GetMapping("/saveToken/{id}/{token}")
	public void token(@PathVariable String id,
			@PathVariable String token) {
		tokenService.storeRedisToken(id, token);
	}

	@GetMapping("/tokens")
	public List<StoredRedisToken> getAllTokens() {
		return tokenService.getAllTokens();
	}
	@GetMapping("/token/{token}")
	public StoredRedisToken getToken(@PathVariable String token) {
		return tokenService.getToken(token);
	}
}
