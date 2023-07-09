/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.service;

import com.ementor.userservice.core.redis.repo.StoredRedisTokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	private final StoredRedisTokenRepo storedRedisTokenRepo;

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		jwt = authHeader.substring(7);
		var storedToken = storedRedisTokenRepo.findByToken(jwt)
			.orElse(null);
		if (storedToken != null) {
			storedToken.setRevoked(true);
			storedRedisTokenRepo.save(storedToken);
			SecurityContextHolder.clearContext();
		}
	}
}
