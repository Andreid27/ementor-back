/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.dto.UniversityDTO;
import com.ementor.profile.service.service.UniversityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/university")
@RequiredArgsConstructor
public class UniversityController {
	private final UniversityService service;

	@GetMapping("/get")
	@Operation(summary = "Get all universities")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<List<UniversityDTO>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
	@GetMapping("/{id}")
	@Operation(summary = "Get university")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<UniversityDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new university.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void register(@RequestBody @Valid UniversityDTO dto) {
		service.createUniversity(dto);
	}

}