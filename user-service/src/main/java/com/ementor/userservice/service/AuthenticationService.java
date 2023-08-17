/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.redis.repo.StoredRedisTokenRepo;
import com.ementor.userservice.core.service.JwtService;
import com.ementor.userservice.dto.*;
import com.ementor.userservice.entity.User;
import com.ementor.userservice.enums.RoleEnum;
import com.ementor.userservice.repo.UsersRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(getClass());

	public AuthenticationNoProfileResponseDTO register(RegisterRequestDTO request) {
		User savedUser = null;
		User user = User.builder()
			.firstName(request.getFirstName())
			.lastName(request.getLastName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.phone(request.getPhone())
			.role(RoleEnum.STUDENT)
			.active(true)
			.disabled(false)
			.hasProfile(false)
			.build();
		try {
			savedUser = repository.save(user);
		} catch (Exception e) {
			log.info("The user could not be saved! The user email maybe already in use.");
			return null;
		}
		String jwtToken = jwtService.generateToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);
		try {
			saveUserToken(savedUser, jwtToken);
		} catch (Exception e) {
			log.error("Cannot save the user token with id: [USER-ID:{}] and token: {} to the redis.", user.getId(),
					jwtToken);
			return null;
		}
		return AuthenticationNoProfileResponseDTO.builder()
			.accessToken(jwtToken)
			.refreshToken(refreshToken)
			.hasProfile(false)
			.build();
	}

	public Object authenticate(AuthenticationRequestDTO request) {
		authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		User storedUser = repository.findByEmail(request.getEmail())
			.orElseThrow();
		String jwtToken = jwtService.generateToken(storedUser);
		String refreshToken = jwtService.generateRefreshToken(storedUser);
		revokeAllUserTokens(storedUser);
		saveUserToken(storedUser, jwtToken);

		boolean hasProfile = storedUser.getHasProfile();
		if (!hasProfile) {
			return AuthenticationNoProfileResponseDTO.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.userData(UserGetDTO.builder()
					.firstName(storedUser.getFirstName())
					.lastName(storedUser.getLastName())
					.email(storedUser.getEmail())
					.role(storedUser.getRole())
					.build())
				.hasProfile(false)
				.build();
		}

		return AuthenticationResponseDTO.builder()
			.accessToken(jwtToken)
			.refreshToken(refreshToken)
			.userData(UserGetDTO.builder()
				.firstName(storedUser.getFirstName())
				.lastName(storedUser.getLastName())
				.email(storedUser.getEmail())
				.role(storedUser.getRole())
				.build())
			.build();
	}

	private void saveUserToken(User user,
			String jwtToken) {
		var token = StoredRedisToken.builder()
			.id(user.getEmail())
			.userId(user.getId())
			.token(jwtToken)
			.build();
		storedRedisTokenRepo.save(token);
	}

	private void revokeAllUserTokens(User user) {
		var validUserTokens = storedRedisTokenRepo.findAllValidTokenById(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> token.setRevoked(true));
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
				var authResponse = AuthenticationResponseDTO.builder()
					.accessToken(accessToken)
					.refreshToken(refreshToken)
					.build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

	public Boolean checkMailAvailability(String email) {
		log.info("Check user email availability {}.", email);

		Boolean available = !repository.existsByEmail(email);

		log.info("Checked user email availability {}.", email);
		return available;
	}
}
