/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity;

import com.ementor.profile.service.core.service.JwtService;
import com.ementor.profile.service.entity.User;
import com.ementor.profile.service.enums.RoleEnum;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final JwtService jwtService;

	@Override // name must be this because of override, but it needs the token
	public User loadUserByUsername(String token) throws UsernameNotFoundException {
		String email = jwtService.extractUsername(token);
		RoleEnum role = RoleEnum.valueOf(jwtService.extractClaim(token, claims -> claims.get("ROLE", String.class)));
		UUID tokenUserId = UUID.fromString(jwtService.extractClaim(token, claims -> claims.get("userId", String.class)));

		return User.builder()
			.email(email)
			.role(role)
			.userId(tokenUserId)
			.build();
	}
}
