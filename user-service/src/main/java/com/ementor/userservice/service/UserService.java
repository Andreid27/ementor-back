/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

import com.ementor.userservice.core.entity.pagination.*;
import com.ementor.userservice.core.entity.pagination.filter.FilterGroup;
import com.ementor.userservice.core.entity.pagination.filter.FilterOption;
import com.ementor.userservice.core.entity.pagination.filter.FilterOptionUtils;
import com.ementor.userservice.core.entity.pagination.filter.FilterType;
import com.ementor.userservice.core.exceptions.EmentorApiError;
import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.redis.repo.StoredRedisTokenRepo;
import com.ementor.userservice.core.service.JwtService;
import com.ementor.userservice.core.service.SecurityService;
import com.ementor.userservice.dto.*;
import com.ementor.userservice.entity.User;
import com.ementor.userservice.enums.RoleEnum;
import com.ementor.userservice.repo.UsersRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UsersRepo repository;
	private final StoredRedisTokenRepo storedRedisTokenRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final EntityManager entityManager;
	private final SecurityService securityService;
	private final Logger log = LoggerFactory.getLogger(getClass());

	public PaginatedResponse<UserGetDTO> getPaginated(PaginatedRequest request) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
		User user = securityService.getCurrentUser();

		log.info("[USER-ID:{}] Getting users list - page {}", user.getId(), request.getPage());

		final PaginatedRequestSpecification<User> spec = PaginatedRequestSpecificationUtils
			.genericSpecification(request.bind(User.class), false, User.class);
		Page<User> findAll = repository.findAll(spec, ServiceUtils.convertToPageRequest(request));
		PaginatedResponse<UserGetDTO> paginatedResponse = new PaginatedResponse<>();
		paginatedResponse.setData(findAll.getContent()
			.stream()
			.map(this::buildUserDto)
			.toList());
		ServiceUtils.extractPaginationMetadata(findAll, paginatedResponse);
		paginatedResponse.setFilterOptions(filterOptions(request));
		paginatedResponse.setCurrentRequest(request);
		log.info("[USER-ID:{}] Got users list - page {}", user.getId(), request.getPage());
		return paginatedResponse;
	}

	public List<FilterOption<?>> filterOptions(PaginatedRequest request) {
		return FilterOptionUtils.createFilterOptions(entityManager, request, User.class,
				new FilterGroup("email", FilterType.TEXT_CONTENT), new FilterGroup("role", FilterType.TEXT_OPTIONS),
				new FilterGroup("lastName", FilterType.TEXT_CONTENT), new FilterGroup("firstName", FilterType.TEXT_CONTENT),
				new FilterGroup("phone", FilterType.TEXT_CONTENT));
	}

	private UserGetDTO buildUserDto(User user) {
		return UserGetDTO.builder()
			.email(user.getEmail())
			.lastName(user.getLastName())
			.firstName(user.getFirstName())
			.role(user.getRole())
			.phone(user.getPhone())
			.build();
	}

	public UserGetDTO get(UUID userId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getId();
		if (userId != null) {
			securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
			return getUserGetDtoByUserId(userId);
		}

		return getUserGetDtoByUserId(currentUserId);
	}

	public UserGetDTO getUserGetDtoByUserId(UUID userId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getId();

		log.info("[USER-ID: {}] Getting user with id {}.", currentUserId, userId);

		User user = getUserByUserId(userId);
		UserGetDTO userGetDTO = buildUserDto(user);
		userGetDTO.setActive(user.getActive());
		userGetDTO.setDisabled(user.getDisabled());
		userGetDTO.setHasProfile(user.getHasProfile());

		log.info("[USER-ID: {}] Got users with id {}.", currentUserId, userId);
		return userGetDTO;
	}

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
			.userData(UserGetDTO.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.role(user.getRole())
				.hasProfile(user.getHasProfile())
				.build())
			.refreshToken(refreshToken)
			.hasProfile(false)
			.build();
	}

	public void updateUserData(UserUpdateDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR, RoleEnum.STUDENT);
		UUID currentUserId = securityService.getCurrentUser()
			.getId();

		log.info("[USER-ID: {}] Updating user profile.", currentUserId);

		User user = getUserByUserId(dto.getUserId());
		user.setPhone(dto.getPhone());
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());

		repository.save(user);

		log.info("[USER-ID: {}] Updated  student profile.", currentUserId);
	}

	public void delete(UUID userId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getId();
		if (userId != null) {
			securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
			deleteUserById(userId);
			return;
		}
		deleteUserById(currentUserId);
	}

	private void deleteUserById(UUID userId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getId();
		log.info("[USER-ID:{}] Deleting user with id: {}", currentUserId, userId);

		User user = getUserByUserId(userId);

		if (user.getExpires() == null) {
			user.setExpires(OffsetDateTime.now());
			user.setDisabled(true);
			user.setActive(false);
		} else {
			throw new EmentorApiError("Cannot delete an already deleted item!", 400);
		}

		user = repository.save(user);
		log.info("[USER-ID:{}] Deleted workpoint with id: {}", currentUserId, user.getId());
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
		if (Boolean.TRUE.equals(!storedUser.getActive() || storedUser.getDisabled()) || !storedUser.isAccountNonExpired()) {
			throw new EmentorApiError("User disabled or non activated. Please contact the administrator.", 401);
		}

		return AuthenticationResponseDTO.builder()
			.accessToken(jwtToken)
			.refreshToken(refreshToken)
			.userData(UserGetDTO.builder()
				.firstName(storedUser.getFirstName())
				.lastName(storedUser.getLastName())
				.email(storedUser.getEmail())
				.role(storedUser.getRole())
				.hasProfile(storedUser.getHasProfile() != true ? false : null)
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

	public User getUserByUserId(UUID userId) {
		return repository.findById(userId)
			.orElseThrow(() -> new EmentorApiError("User not found"));
	}

	public User getUserByEmail(String email) {
		return repository.findByEmail(email)
			.orElseThrow(() -> new EmentorApiError("User not found"));
	}
}
