/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_answers")
public class UserAnswer extends CommonEntity {

	@Column(name = "quiz_id")
	private UUID quizId;

	@Column(name = "question_id")
	private UUID questionId;

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "answer", columnDefinition = "SMALLINT")
	private Short answer;

	@Column(name = "correct_answer", columnDefinition = "SMALLINT")
	private Short correctAnswer;

}
