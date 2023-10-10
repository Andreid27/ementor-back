/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.OffsetDateTime;
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
public class AssignQuizDTO {

	private UUID quizId;

	private UUID userId;

	private OffsetDateTime startAfter;

	private OffsetDateTime expires;
}
