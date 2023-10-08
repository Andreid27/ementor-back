/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.enums.QuizComponentType;
import jakarta.persistence.*;
import java.io.Serial;
import java.io.Serializable;
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
@Table(name = "quizzes_view")
public class QuizzesView implements Serializable {
	@Serial
	private static final long serialVersionUID = 7174219640626269429L;

	@Id
	@Column(name = "quiz_id")
	private UUID id;

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
	private Short difficultyLevel;

	@Column(name = "question_count")
	private Integer questionsCount;

	@Column(name = "max_time")
	private Integer maxTime;

	@Column(name = "created_by")
	private UUID createdBy;

}