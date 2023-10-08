/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.service;

import com.ementor.quiz.core.entity.pagination.*;
import com.ementor.quiz.core.exceptions.EmentorApiError;
import com.ementor.quiz.core.service.SecurityService;
import com.ementor.quiz.dto.ChapterDTO;
import com.ementor.quiz.dto.QuestionDTO;
import com.ementor.quiz.dto.QuizDTO;
import com.ementor.quiz.entity.*;
import com.ementor.quiz.enums.RoleEnum;
import com.ementor.quiz.repo.QuizzesRepo;
import com.ementor.quiz.repo.QuizzesViewRepo;
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
public class QuizzesService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final EntityManager entityManager;

	private final SecurityService securityService;

	private final ChaptersService chaptersService;

	private final QuestionsService questionsService;

	private final QuizzesRepo quizzesRepo;

	private final QuizzesViewRepo quizzesViewRepo;

	public PaginatedResponse<QuizzesView> getPaginated(PaginatedRequest request) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
		User user = securityService.getCurrentUser();
		String filterCriteria = "createdBy";

		log.info("[USER-ID:{}] Getting quizzes list - page {}", user.getUserId(), request.getPage());

		final PaginatedRequestSpecification<QuizzesView> spec = PaginatedRequestSpecificationUtils
			.genericSpecification(request.bind(QuizzesView.class), false, QuizzesView.class);
		if (user.getRole()
			.equals(RoleEnum.PROFESSOR)) {
			PaginationUtils.addFilterCriteria(request, filterCriteria, user.getUserId());
		}
		Page<QuizzesView> findAll = quizzesViewRepo.findAll(spec, ServiceUtils.convertToPageRequest(request));
		PaginatedResponse<QuizzesView> paginatedResponse = new PaginatedResponse<>();
		if (user.getRole()
			.equals(RoleEnum.PROFESSOR)) {
			PaginationUtils.removeFilterCriteria(request, filterCriteria);
		}
		paginatedResponse.setData(findAll.getContent()
			.stream()
			.toList());
		ServiceUtils.extractPaginationMetadata(findAll, paginatedResponse);
		paginatedResponse.setCurrentRequest(request);

		log.info("[USER-ID:{}] Got quizzes list - page {}", user.getUserId(), request.getPage());

		return paginatedResponse;
	}

	public QuizDTO getQuiz(UUID quizId) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR);

		User currentUser = securityService.getCurrentUser();

		log.info("[USER-ID: {}] Getting quiz.", currentUser.getUserId());
		Quiz quiz = getQuizById(quizId);
		List<ChapterDTO> chapterDTOList = buildChaptersDtoList(quiz.getChapters(), currentUser);
		List<QuestionDTO> questionDTOList = buildQuestionDtoList(quiz.getQuestions(), currentUser);

		log.info("[USER-ID: {}] Got quiz.", currentUser.getUserId());

		return QuizDTO.builder()
			.id(quiz.getId())
			.title(quiz.getTitle())
			.description(quiz.getDescription())
			.componentType(quiz.getComponentType())
			.difficultyLevel(quiz.getDifficultyLevel())
			.maxTime(quiz.getMaxTime())
			.chapters(chapterDTOList)
			.questions(questionDTOList)
			.createdBy(quiz.getCreatedBy())
			.build();
	}

	private List<ChapterDTO> buildChaptersDtoList(List<Chapter> chapters,
			User currentUser) {
		if (currentUser.getRole()
			.equals(RoleEnum.STUDENT)) {
			return chapters.stream()
				.map(chapter -> ChapterDTO.builder()
					.title(chapter.getTitle())
					.description(chapter.getDescription())
					.build())
				.toList();
		}

		return chapters.stream()
			.map(chapter -> ChapterDTO.builder()
				.id(chapter.getId())
				.title(chapter.getTitle())
				.description(chapter.getDescription())
				.createdBy(chapter.getCreatedBy())
				.build())
			.toList();
	}

	private List<QuestionDTO> buildQuestionDtoList(List<Question> questions,
			User currentUser) {
		if (currentUser.getRole()
			.equals(RoleEnum.STUDENT)) {
			return questions.stream()
				.map(question -> QuestionDTO.builder()
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
					.build())
				.toList();
		}

		return questions.stream()
			.map(question -> QuestionDTO.builder()
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
				.build())
			.toList();
	}

	@Transactional
	public void create(QuizDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating quiz.", currentUserId);

		Quiz quiz = saveQuiz(dto);
		quiz.setCreatedBy(currentUserId);

		quizzesRepo.save(quiz);

		log.info("[USER-ID: {}] Created quiz.", currentUserId);
	}

	private Quiz saveQuiz(QuizDTO dto) {
		List<Chapter> chapters = chaptersService.getChaptersByIds(dto.getChaptersId());
		List<Question> questions = questionsService.getQuestionsByIds(dto.getQuestionsId());
		return Quiz.builder()
			.title(dto.getTitle())
			.description(dto.getDescription())
			.componentType(dto.getComponentType())
			.difficultyLevel(dto.getDifficultyLevel())
			.maxTime(dto.getMaxTime())
			.chapters(chapters)
			.questions(questions)
			.build();
	}

	@Transactional
	public void update(QuizDTO dto) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Updating quiz.", currentUserId);

		Quiz oldQuiz = getQuizById(dto.getId());
		Quiz quiz = saveQuiz(dto);
		quiz.setId(dto.getId());
		quiz.setCreatedBy(oldQuiz.getCreatedBy());

		quizzesRepo.save(quiz);

		log.info("[USER-ID: {}] Updated quiz.", currentUserId);
	}

	public void delete(UUID quizId) {
		securityService.hasAnyRole(RoleEnum.PROFESSOR);

		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Deleting quiz with id {}.", currentUserId, quizId);

		Quiz quiz = getQuizById(quizId);
		quiz.setExpires(OffsetDateTime.now());
		quizzesRepo.save(quiz);

		log.info("[USER-ID: {}] Deleted quiz with id {}.", currentUserId, quizId);
	}
	public Quiz getQuizById(UUID quizId) {
		return quizzesRepo.findById(quizId)
			.orElseThrow(() -> new EmentorApiError("Quiz not found", 404));
	}
}
