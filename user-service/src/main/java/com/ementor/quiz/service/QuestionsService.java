/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.service;

import com.ementor.quiz.core.exceptions.EmentorApiError;
import com.ementor.quiz.core.service.SecurityService;
import com.ementor.quiz.dto.QuestionDTO;
import com.ementor.quiz.entity.Question;
import com.ementor.quiz.enums.RoleEnum;
import com.ementor.quiz.repo.QuestionsRepo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionsService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final QuestionsRepo questionsRepo;

	public QuestionDTO getQuestion(UUID questionId) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR, RoleEnum.STUDENT);

		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting  question.", currentUserId);
		Question question = getQuestionById(questionId);
		log.info("[USER-ID: {}] Got  question.", currentUserId);

		return QuestionDTO.builder()
			.id(question.getId())
			.content(question.getContent())
			.answer1(question.getAnswer1())
			.answer2(question.getAnswer2())
			.answer3(question.getAnswer3())
			.answer4(question.getAnswer4())
			.answer5(question.getAnswer5())
			.correctAnswer(question.getCorrectAnswer())
			.difficultyLevel(question.getDifficultyLevel())
			.source(question.getSource())
			.sourcePage(question.getSourcePage())
			.hint(question.getHint())
			.createdBy(question.getCreatedBy())
			.build();
	}

	private Question getQuestionById(UUID questionId) {
		return questionsRepo.findById(questionId)
			.orElseThrow(() -> new EmentorApiError("Question not found"));
	}
}
