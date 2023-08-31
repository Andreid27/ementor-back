/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.core.entity.pagination.PaginatedRequest;
import com.ementor.profile.service.core.entity.pagination.PaginatedResponse;
import com.ementor.profile.service.dto.ProfilePrerequrireDTO;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.entity.Speciality;
import com.ementor.profile.service.service.StudentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
	private final StudentProfileService service;

	@PostMapping("/paginated")
	@Operation(summary = "Get paginated workpoints")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public PaginatedResponse<Speciality> getPaginated(@RequestBody PaginatedRequest request) {
		return service.getPaginated(request);
	}

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
	@Operation(summary = "Create a new student profile.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void create(@RequestBody @Valid StudentProfileDTO dto) {
		service.createStudentProfile(dto);
	}

	@PutMapping("/update")
	@Operation(summary = "Update a existing student profile.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid StudentProfileDTO dto) {
		service.updateStudentProfile(dto);
	}

	@GetMapping("/profile-prerequire")
	@Operation(summary = "Get current initial data for creating student profile")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<ProfilePrerequrireDTO> getProfilePrerequire() {
		return ResponseEntity.ok(service.getProfilePrerequire());
	}

}
