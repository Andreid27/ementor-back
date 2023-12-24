/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.enums.QuizComponentType;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
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
@Table(name = "quizzes_students_view")
public class QuizzesStudentsView implements Serializable {
	@Serial
	private static final long serialVersionUID = 7174219640626269429L;

	@Id
	@Column(name = "attempt_id")
	private UUID id;

	@Column(name = "user_id")
	private UUID studentId;

	@Column(name = "quiz_id")
	private UUID quizId;

	@Column(name = "quiz_title")
	private String title;

	@Column(name = "quiz_description")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "component_type")
	private QuizComponentType componentType;

	@Column(name = "chapter_titles")
	private String chapterTitles;

	@Column(name = "difficulty_level", columnDefinition = "SMALLINT")
	private Integer difficultyLevel;

	@Column(name = "question_count")
	private Integer questionsCount;

	@Column(name = "max_time")
	private Integer maxTime;

	@Column(name = "correct_answers")
	private Integer correctAnswers;

	@Column(name = "created_by")
	private UUID createdBy;

	@Column(name = "assigned_at")
	private OffsetDateTime assignedAt;

}