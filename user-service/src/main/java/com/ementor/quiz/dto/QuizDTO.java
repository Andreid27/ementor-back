/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.dto;

import com.ementor.quiz.enums.QuizComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {

	private UUID id;

	@NotBlank(message = "Content of the quiz cannot be null or blank")
	@Size(min = 1, max = 200)
	private String title;

	@Size(max = 300)
	private String description;

	private QuizComponentType componentType;

	private Short difficultyLevel;

	private Integer maxTime;

	List<UUID> chaptersId;
	List<ChapterDTO> chapters;

	List<UUID> questionsId;
	List<QuestionDTO> questions;

	private UUID createdBy;
}
