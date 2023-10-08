/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.core.entity.CommonEntity;
import com.ementor.quiz.enums.QuizComponentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quiz extends CommonEntity {
	@Serial
	private static final long serialVersionUID = 7174219640626269429L;

	@Column(name = "title", length = 200)
	@NotBlank(message = "Content of the quiz cannot be null or blank")
	@Size(min = 1, max = 200)
	private String title;

	@Column(name = "description", length = 300)
	@Size(max = 300)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "component_type")
	private QuizComponentType componentType;

	@Column(name = "difficulty_level", columnDefinition = "SMALLINT")
	private Short difficultyLevel;

	@Column(name = "max_time")
	private Integer maxTime;

	@ManyToMany
	@JoinTable(
		name = "quizzes_chapters",
		joinColumns = @JoinColumn(name = "quiz_id"),
		inverseJoinColumns = @JoinColumn(name = "chapter_id"))
	private List<Chapter> chapters;

	@ManyToMany
	@JoinTable(
		name = "quizzes_questions",
		joinColumns = @JoinColumn(name = "quiz_id"),
		inverseJoinColumns = @JoinColumn(name = "question_id"))
	private List<Question> questions;

	@Column(name = "created_by")
	private UUID createdBy;
}
