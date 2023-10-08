/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes_students")
public class QuizStudent extends CommonEntity {
	@Serial
	private static final long serialVersionUID = 3663056751236961171L;

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "quiz_id")
	private UUID quizId;

	@Column(name = "created_by")
	private UUID createdBy;

	@Column(name = "started_at")
	private OffsetDateTime startAt;

	@Column(name = "end_time")
	private OffsetDateTime endTime;

	@Column(name = "ended_time")
	private OffsetDateTime endedTime;

	@Column(name = "correct_answers")
	private Integer correctAnswers;

}
