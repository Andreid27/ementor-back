/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.controller;

import com.ementor.core.service.CoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class CoreController {
	// Everything here is authentificated

	private final CoreService service;

	@PostMapping("/date")
	@Operation(summary = "Get request")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<String> register(@RequestBody String request) {
		return ResponseEntity.ok(service.localDateTimeLogger());
	}

}
