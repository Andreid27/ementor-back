/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

import com.ementor.userservice.config.JwtService;
import com.ementor.userservice.dto.AuthenticationRequest;
import com.ementor.userservice.dto.AuthenticationResponse;
import com.ementor.userservice.dto.RegisterRequest;
import com.ementor.userservice.entity.User;
import com.ementor.userservice.enums.TokenTypeEnum;
import com.ementor.userservice.redis.entity.StoredRedisToken;
import com.ementor.userservice.redis.repo.StoredRedisTokenRepo;
import com.ementor.userservice.repo.UsersRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TimeZone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UsersRepo repository;
	private final StoredRedisTokenRepo storedRedisTokenRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		User user = User.builder()
			.firstName(request.getFirstName())
			.lastName(request.getLastName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(request.getRole())
			.active(true)
			.disabled(false)
			.timezone(TimeZone.getDefault())
			.build();
		User savedUser = repository.save(user);
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(refreshToken)
			.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User user = repository.findByEmail(request.getEmail())
			.orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder()
			.accessToken(jwtToken)
			.refreshToken(refreshToken)
			.build();
	}

	private void saveUserToken(User user,
			String jwtToken) {
		var token = StoredRedisToken.builder()
			.userName(user.getEmail())
			.userId(user.getId())
			.token(jwtToken)
			.tokenType(TokenTypeEnum.BEARER)
			.expired(false)
			.revoked(false)
			.build();
		storedRedisTokenRepo.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = storedRedisTokenRepo.findAllValidTokenByUserId(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		storedRedisTokenRepo.saveAll(validUserTokens);
	}

	public void refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.repository.findByEmail(userEmail)
				.orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder()
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
}
