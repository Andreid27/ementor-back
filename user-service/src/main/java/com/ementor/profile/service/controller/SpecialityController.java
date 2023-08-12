/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.dto.SpecialityDTO;
import com.ementor.profile.service.service.SpecialityService;
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
@RequestMapping("/speciality")
@RequiredArgsConstructor
public class SpecialityController {
	private final SpecialityService service;

	@GetMapping("/get")
	@Operation(summary = "Get all specialities")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<List<SpecialityDTO>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get speciality")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<SpecialityDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.get(id));
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new speciality.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void create(@RequestBody @Valid SpecialityDTO dto) {
		service.createSpeciality(dto);
	}

	@PutMapping("/update/{id}")
	@Operation(summary = "Update a speciality.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void register(@RequestBody @Valid SpecialityDTO dto,
			@PathVariable UUID id) {
		service.updateSpeciality(dto, id);
	}

	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete a speciality by ID.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "204", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request"),
				@ApiResponse(responseCode = "404", description = "Speciality not found")})
	public ResponseEntity<Void> delete(@PathVariable UUID id) {
		service.deleteSpeciality(id);
		return ResponseEntity.noContent()
			.build();
	}

}