/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.entity;

import com.ementor.quiz.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
public class Question extends CommonEntity {
	@Serial
	private static final long serialVersionUID = -6985313948583609973L;

	@Column(name = "content", length = 100)
	@NotBlank(message = "Content of the question cannot be null or blank")
	@Size(min = 1, max = 500)
	private String content;

	@Column(name = "answer1", length = 300)
	@Size(max = 300)
	private String answer1;

	@Column(name = "answer2", length = 300)
	@Size(max = 300)
	private String answer2;

	@Column(name = "answer3", length = 300)
	@Size(max = 300)
	private String answer3;

	@Column(name = "answer4", length = 300)
	@Size(max = 300)
	private String answer4;

	@Column(name = "answer5", length = 300)
	@Size(max = 300)
	private String answer5;

	@Column(name = "correct_answer", columnDefinition = "SMALLINT")
	private Short correctAnswer;

	@Column(name = "source", length = 300)
	@Size(max = 300)
	private String source;

	@Column(name = "source_page")
	private Integer sourcePage;

	@Column(name = "difficulty_level", columnDefinition = "SMALLINT")
	private Short difficultyLevel;

	@Column(name = "hint", length = 300)
	@Size(max = 300)
	private String hint;

	@Column(name = "created_by")
	private UUID createdBy;

}
