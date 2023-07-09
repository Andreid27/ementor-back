/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.service;

import com.ementor.userservice.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext()
			.getAuthentication();
	}

	public boolean hasRole(String role) {
		Authentication auth = getAuthentication();
		// TODO to make for any role;
		if (auth != null) {
			return auth.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role::equals);
		}
		return false;
	}

	public User getCurrentUser() {
		Authentication authentication = getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
