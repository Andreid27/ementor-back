/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.controller;

import com.ementor.quiz.core.entity.pagination.PaginatedRequest;
import com.ementor.quiz.core.entity.pagination.PaginatedResponse;
import com.ementor.quiz.dto.QuizDTO;
import com.ementor.quiz.dto.SubmitQuizDTO;
import com.ementor.quiz.entity.QuizzesView;
import com.ementor.quiz.service.QuizzesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizzesController {

	private final QuizzesService service;

	@PostMapping("/paginated")
	@Operation(summary = "Get paginated quizzes")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public PaginatedResponse<QuizzesView> getPaginated(@RequestBody PaginatedRequest request) {
		return service.getPaginated(request);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get quiz by id")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<QuizDTO> get(@PathVariable UUID id) {
		return ResponseEntity.ok(service.getQuiz(id));
	}

	@PostMapping("/create")
	@Operation(summary = "Create a new quiz.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void create(@RequestBody @Valid QuizDTO dto) {
		service.create(dto);
	}

	@PutMapping("/update")
	@Operation(summary = "Update an existing quiz.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void update(@RequestBody @Valid QuizDTO dto) {
		service.update(dto);
	}

	@DeleteMapping(value = {"/delete/{id}"})
	@Operation(summary = "Delete an existing quiz.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void delete(@PathVariable UUID id) {
		service.delete(id);
	}

	@GetMapping("/start-quiz/{id}")
	@Operation(summary = "Start quiz by id")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<QuizDTO> start(@PathVariable UUID id) {
		return ResponseEntity.ok(service.startQuiz(id));
	}

	@PostMapping("/submit-quiz")
	@Operation(summary = "Submit a quiz.")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public ResponseEntity<SubmitQuizDTO> submit(@RequestBody @Valid SubmitQuizDTO dto) {
		return ResponseEntity.ok(service.submit(dto));
	}
}
