/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.controller;

import com.ementor.quiz.core.entity.pagination.PaginatedRequest;
import com.ementor.quiz.core.entity.pagination.PaginatedResponse;
import com.ementor.quiz.dto.ChapterDTO;
import com.ementor.quiz.entity.Chapter;
import com.ementor.quiz.service.ChaptersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChaptersController {
	private final ChaptersService service;

	@PostMapping("/paginated")
	@Operation(summary = "Get paginated chapters")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public PaginatedResponse<Chapter> getPaginated(@RequestBody PaginatedRequest request) {
		return service.getPaginated(request);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get chapter by id")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<ChapterDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getChapter(id));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/create")
	@Operation(summary = "Create a new chapter.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "201", description = "Request successful"),
				@ApiResponse(responseCode = "500", description = "Invalid request")})
	public void create(@RequestBody @Valid ChapterDTO dto) {
		service.create(dto);
	}

	@PutMapping("/update")
	@Operation(summary = "Update an existing chapter.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid ChapterDTO dto) {
		service.update(dto);
	}

	@DeleteMapping(value = {"/delete/{id}"})
	@Operation(summary = "Delete an existing chapter.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
}
