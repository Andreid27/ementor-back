/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.service;

import com.ementor.userservice.core.exceptions.EmentorApiError;
import com.ementor.userservice.entity.User;
import com.ementor.userservice.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SecurityService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext()
			.getAuthentication();
	}

	public boolean hasRole(RoleEnum role) throws EmentorApiError {
		Authentication auth = getAuthentication();
		boolean hasRole = false;
		if (auth != null) {
			hasRole = auth.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.anyMatch(role.name()::equals);
		}
		if (!hasRole) {
			throw new EmentorApiError("Current user: {" + getCurrentUser().getEmail() + "} does not have role: " + role);
		}

		return hasRole;
	}

	public boolean hasAnyRole(RoleEnum... allowedRoles) throws EmentorApiError {
		Authentication auth = getAuthentication();
		boolean hasRole = false;
		if (auth != null) {
			hasRole = Arrays.stream(allowedRoles)
				.map(RoleEnum::name)
				.anyMatch(roleName -> auth.getAuthorities()
					.stream()
					.map(GrantedAuthority::getAuthority)
					.anyMatch(roleName::equals));
		}
		if (!hasRole) {
			throw new EmentorApiError(
					"Current user: {" + getCurrentUser().getEmail() + "} does not have  any role: " + Arrays.stream(allowedRoles).map(Enum::name).toList(), 401);
		}

		return hasRole;
	}

	public User getCurrentUser() {
		Authentication authentication = getAuthentication();
		User user = null;
		try {
			user = (User) authentication.getPrincipal();
		} catch (Exception e) {
			log.error("No user in auth context. Err: {}", e.getMessage());
			throw new EmentorApiError("No current user logged in!", 403);
		}
		return user;
	}
}
