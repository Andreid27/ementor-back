/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.controller;

import com.ementor.quiz.core.entity.pagination.PaginatedRequest;
import com.ementor.quiz.core.entity.pagination.PaginatedResponse;
import com.ementor.quiz.dto.QuestionDTO;
import com.ementor.quiz.entity.Question;
import com.ementor.quiz.service.QuestionsService;
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
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionsController {
	// Everything here is authentificated

	private final QuestionsService service;

	@PostMapping("/paginated")
	@Operation(summary = "Get paginated questions")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public PaginatedResponse<Question> getPaginated(@RequestBody PaginatedRequest request) {
		return service.getPaginated(request);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get question by id")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<QuestionDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getQuestion(id));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/create")
	@Operation(summary = "Create a new question.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void create(@RequestBody @Valid QuestionDTO dto) {
		service.create(dto);
	}

	@PutMapping("/update")
	@Operation(summary = "Update a existing student profile.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid QuestionDTO dto) {
		service.update(dto);
	}

	@DeleteMapping(value = {"/delete/{id}"})
	@Operation(summary = "Delete a existing question.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}
}
