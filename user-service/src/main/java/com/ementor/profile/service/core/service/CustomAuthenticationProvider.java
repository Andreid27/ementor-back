/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.service;

import com.ementor.profile.service.core.entity.CustomUserDetailsService;
import com.ementor.profile.service.core.redis.entity.StoredRedisToken;
import com.ementor.profile.service.core.redis.repo.StoredRedisTokenRepo;
import com.ementor.profile.service.entity.User;
import com.ementor.profile.service.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private final CustomUserDetailsService userDetailsService;

	private final StoredRedisTokenRepo storedRedisTokenRepo;

	private final JwtService jwtService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();

		User user = userDetailsService.loadUserByUsername(email);
		return checkStoredRedisUserId(user, email);
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
	}

	private Authentication checkStoredRedisUserId(User user,
			String email) {
		if (checkIfAllMatch(user, email)) {
			return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
		} else {
			throw new BadCredentialsException("Bad credentials");
		}
	}

	public boolean checkIfAllMatch(User user,
			String email) {
		StoredRedisToken storedRedisToken = storedRedisTokenRepo.findById(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found in redis cache: " + email));

		Boolean userIdIsSame = user.getUserId()
			.equals(storedRedisToken.getUserId());
		Boolean roleIsSame = user.getRole()
			.equals(RoleEnum
				.valueOf(jwtService.extractClaim(storedRedisToken.getToken(), claims -> claims.get("ROLE", String.class))));
		Boolean emailIsSame = user.getEmail()
			.equals(storedRedisToken.getId());

		return userIdIsSame && roleIsSame && emailIsSame;
	}
}
