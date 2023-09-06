/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.controller;

import com.ementor.userservice.core.entity.pagination.PaginatedRequest;
import com.ementor.userservice.core.entity.pagination.PaginatedResponse;
import com.ementor.userservice.dto.*;
import com.ementor.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@PostMapping("/paginated")
	@Operation(summary = "Get paginated users")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public PaginatedResponse<UserGetDTO> getPaginated(@RequestBody PaginatedRequest request) {
		return service.getPaginated(request);
	}

	@GetMapping(value = {"/get", "/{userId}"})
	@Operation(summary = "Get user profile")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<UserGetDTO> get(@PathVariable(required = false) UUID userId) {
		return ResponseEntity.ok(service.get(userId));
	}

	@PostMapping("/register")
	@Operation(summary = "Get authentication tokens and user data.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<AuthenticationNoProfileResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
		return ResponseEntity.ok(service.register(request));
	}

	@PutMapping("/update")
	@Operation(summary = "Update a existing user.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid UserUpdateDTO dto) {
		service.updateUserData(dto);
	}

	@DeleteMapping(value = {"/delete", "/delete/{userId}"})
	@Operation(summary = "Delete a existing user.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void delete(@PathVariable UUID userId) {
		service.delete(userId);
	}

	@PostMapping("/authenticate")
	public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequestDTO request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}

	@GetMapping("/check-availability/{email}")
	@Operation(summary = "Check if a email is available")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<Boolean> get(@PathVariable String email) {
		return ResponseEntity.ok(service.checkMailAvailability(email));
	}

}
