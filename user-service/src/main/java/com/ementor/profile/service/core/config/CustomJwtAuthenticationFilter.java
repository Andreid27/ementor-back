/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.config;

import com.ementor.profile.service.core.entity.CustomUserDetailsService;
import com.ementor.profile.service.core.service.CustomAuthenticationProvider;
import com.ementor.profile.service.core.service.JwtService;
import com.ementor.profile.service.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final CustomUserDetailsService customUserDetailsService;

	private final CustomAuthenticationProvider customAuthenticationProvider;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException,
			IOException {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String userEmail;

		// logic of login without user_service databases. Case: 1. only with
		// userId and email
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		jwt = authHeader.substring(7);
		userEmail = jwtService.extractUsername(jwt);

		if (userEmail != null && SecurityContextHolder.getContext()
			.getAuthentication() == null) {
			User user = this.customUserDetailsService.loadUserByUsername(jwt);
			if (jwtService.isTokenValid(jwt, user) && Boolean.TRUE.equals(customAuthenticationProvider.checkIfAllMatch(user, userEmail))) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext()
					.setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
