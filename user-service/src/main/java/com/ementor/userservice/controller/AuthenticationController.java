/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.controller;

import com.ementor.userservice.dto.AuthenticationRequestDTO;
import com.ementor.userservice.dto.AuthenticationResponseDTO;
import com.ementor.userservice.dto.RegisterRequestDTO;
import com.ementor.userservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	@Operation(summary = "Get request")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody RegisterRequestDTO request) {
		return ResponseEntity.ok(service.register(request));
	}
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}

}
