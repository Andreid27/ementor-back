/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.controller;

import com.ementor.userservice.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/profile-data")
@RequiredArgsConstructor
public class UserProfileController {
	private final ProfileService service;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload a new image")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<UUID> upload(@RequestPart(name = "file") MultipartFile file) {
		return ResponseEntity.ok(service.saveProfilePicture(file));
	}

	@GetMapping("/download/{fileId}")
	@Operation(summary = "Download file")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<Resource> download(@PathVariable(name = "fileId") UUID fileId) {
		return service.download(fileId);
	}

	@GetMapping("/profile-completed/{isProfileComplete}")
	@Operation(summary = "Check if a email is available")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<Boolean> get(@PathVariable Boolean isProfileComplete) {
		return ResponseEntity.ok(service.completeProfile(isProfileComplete));
	}
}
