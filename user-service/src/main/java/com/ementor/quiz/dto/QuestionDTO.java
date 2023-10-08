/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

	private UUID id;

	@NotBlank(message = "Content of the question cannot be null or blank")
	@Size(min = 1, max = 500)
	private String content;

	@Size(max = 300)
	private String answer1;

	@Size(max = 300)
	private String answer2;

	@Size(max = 300)
	private String answer3;

	@Size(max = 300)
	private String answer4;

	@Size(max = 300)
	private String answer5;

	private Short correctAnswer;

	@Size(max = 300)
	private String source;

	private Integer sourcePage;

	private Short difficultyLevel;

	@Size(max = 300)
	private String hint;

	private UUID createdBy;
}
