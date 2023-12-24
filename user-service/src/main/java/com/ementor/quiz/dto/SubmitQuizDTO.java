/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
import java.util.List;
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
public class SubmitQuizDTO {

	private UUID quizStudentId;

	private List<SubmitedQuestionAnswer> submitedQuestionAnswers;

	private QuizDTO quiz;

	private List<SubmitedQuestionAnswer> correctAnswers;

	private OffsetDateTime startedAt;

	private OffsetDateTime enddedAt;

	private Integer correctCount;

	private UUID studentId;
}
