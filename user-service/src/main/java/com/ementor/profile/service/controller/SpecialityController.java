/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.dto.SpecialitySendDTO;
import com.ementor.profile.service.service.SpecialityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/speciality")
@RequiredArgsConstructor
public class SpecialityController {
	private final SpecialityService service;

	@PostMapping("/create")
	@Operation(summary = "Create a new speciality.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void register(@RequestBody @Valid SpecialitySendDTO dto) {
		service.createSpeciality(dto);
	}

}