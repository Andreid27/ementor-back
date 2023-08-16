/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.dto.ProfilePrerequrireDTO;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.service.StudentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
	private final StudentProfileService service;

	@GetMapping("/{id}")
	@Operation(summary = "Get student profile")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<StudentProfileDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@GetMapping("/get")
	@Operation(summary = "Get current student profile")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<StudentProfileDTO> getUserProfile() {
		return ResponseEntity.ok(service.getUserProfile());
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new speciality.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void create(@RequestBody @Valid StudentProfileDTO dto) {
		service.createStudentProfile(dto);
	}

	@PutMapping("/update")
	@Operation(summary = "Create a new speciality.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid StudentProfileDTO dto) {
		service.updateStudentProfile(dto);
	}

	@GetMapping("/profile-prerequire")
	@Operation(summary = "Get current student profile")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<ProfilePrerequrireDTO> getProfilePrerequire() {
		return ResponseEntity.ok(service.getProfilePrerequire());
	}

}
