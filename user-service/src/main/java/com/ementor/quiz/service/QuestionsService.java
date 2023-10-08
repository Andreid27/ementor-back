/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.service;

import com.ementor.quiz.core.entity.pagination.*;
import com.ementor.quiz.core.entity.pagination.filter.FilterGroup;
import com.ementor.quiz.core.entity.pagination.filter.FilterOption;
import com.ementor.quiz.core.entity.pagination.filter.FilterOptionUtils;
import com.ementor.quiz.core.entity.pagination.filter.FilterType;
import com.ementor.quiz.core.exceptions.EmentorApiError;
import com.ementor.quiz.core.service.SecurityService;
import com.ementor.quiz.dto.QuestionDTO;
import com.ementor.quiz.entity.Question;
import com.ementor.quiz.entity.User;
import com.ementor.quiz.enums.RoleEnum;
import com.ementor.quiz.repo.QuestionsRepo;
import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionsService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final EntityManager entityManager;

	private final SecurityService securityService;

	private final QuestionsRepo questionsRepo;

	public PaginatedResponse<Question> getPaginated(PaginatedRequest request) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
		User user = securityService.getCurrentUser();

		log.info("[USER-ID:{}] Getting questions list - page {}", user.getUserId(), request.getPage());

		final PaginatedRequestSpecification<Question> spec = PaginatedRequestSpecificationUtils
			.genericSpecification(request.bind(Question.class), false, Question.class);
		Page<Question> findAll = questionsRepo.findAll(spec, ServiceUtils.convertToPageRequest(request));
		PaginatedResponse<Question> paginatedResponse = new PaginatedResponse<>();
		paginatedResponse.setData(findAll.getContent()
			.stream()
			.toList());
		ServiceUtils.extractPaginationMetadata(findAll, paginatedResponse);
		paginatedResponse.setFilterOptions(filterOptions(request));
		paginatedResponse.setCurrentRequest(request);
		log.info("[USER-ID:{}] Got questions list - page {}", user.getUserId(), request.getPage());
		return paginatedResponse;
	}

	public List<FilterOption<?>> filterOptions(PaginatedRequest request) {
		return FilterOptionUtils.createFilterOptions(entityManager, request, Question.class,
				new FilterGroup("difficultyLevel", FilterType.NUMBER_OPTIONS),
				new FilterGroup("content", FilterType.TEXT_CONTENT), new FilterGroup("hint", FilterType.TEXT_CONTENT));
	}

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

	@Transactional
	public void create(QuestionDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  question.", currentUserId);

		Question question = saveQuestion(dto);
		question.setCreatedBy(currentUserId);

		questionsRepo.save(question);

		log.info("[USER-ID: {}] Created  question.", currentUserId);
	}

	private Question saveQuestion(QuestionDTO dto) {
		return Question.builder()
			.content(dto.getContent())
			.answer1(dto.getAnswer1())
			.answer2(dto.getAnswer2())
			.answer3(dto.getAnswer3())
			.answer4(dto.getAnswer4())
			.answer5(dto.getAnswer5())
			.correctAnswer(dto.getCorrectAnswer())
			.difficultyLevel(dto.getDifficultyLevel())
			.source(dto.getSource())
			.sourcePage(dto.getSourcePage())
			.hint(dto.getHint())
			.build();
	}

	@Transactional
	public void update(QuestionDTO dto) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Updating  question.", currentUserId);

		Question oldQuestion = getQuestionById(dto.getId());
		Question question = saveQuestion(dto);
		question.setId(dto.getId());
		question.setCreatedBy(oldQuestion.getCreatedBy());

		questionsRepo.save(question);

		log.info("[USER-ID: {}] Updated  question.", currentUserId);
	}

	public void delete(UUID questionId) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR);

		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Deleting  question with id {}.", currentUserId, questionId);

		Question question = getQuestionById(questionId);
		question.setExpires(OffsetDateTime.now());
		questionsRepo.save(question);

		log.info("[USER-ID: {}] Deleted  question with id {}.", currentUserId, questionId);
	}

	public Question getQuestionById(UUID questionId) {
		return questionsRepo.findById(questionId)
			.orElseThrow(() -> new EmentorApiError("Question not found", 404));
	}

	public List<Question> getQuestionsByIds(List<UUID> questionsIds) {
		return questionsRepo.findAllByIdIn(questionsIds);
	}
}
