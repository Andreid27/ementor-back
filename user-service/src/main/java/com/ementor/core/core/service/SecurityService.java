/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.service;

import com.ementor.core.core.exceptions.EmentorApiError;
import com.ementor.core.entity.User;
import com.ementor.core.enums.RoleEnum;
import java.util.Arrays;
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
					"Current user: {" + getCurrentUser().getEmail() + "} does not have  any role: " + allowedRoles);
		}

		return hasRole;
	}

	public User getCurrentUser() {
		Authentication authentication = getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
