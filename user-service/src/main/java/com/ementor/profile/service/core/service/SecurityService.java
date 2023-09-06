/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.entity.User;
import com.ementor.profile.service.enums.RoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
			throw new EmentorApiError("Current user: {" + getCurrentUser().getEmail() + "} does not have  any role: "
					+ Arrays.stream(allowedRoles)
					.map(Enum::name)
					.toList(),
					401);
		}

		return hasRole;
	}

	public User getCurrentUser() {
		Authentication authentication = getAuthentication();
		return (User) authentication.getPrincipal();
	}
}
