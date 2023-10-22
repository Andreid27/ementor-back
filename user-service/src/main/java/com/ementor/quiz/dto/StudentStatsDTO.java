/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.dto;

import com.ementor.quiz.dto.StudentStatsComponetsDTOs.StudentQuestionsDTO;
import com.ementor.quiz.dto.StudentStatsComponetsDTOs.StudentQuizzesDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatsDTO {

	private StudentQuestionsDTO questions;

	private StudentQuizzesDTO quizzes;

}
