/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.service;

import com.ementor.quiz.core.entity.pagination.*;
import com.ementor.quiz.core.exceptions.EmentorApiError;
import com.ementor.quiz.core.service.SecurityService;
import com.ementor.quiz.dto.*;
import com.ementor.quiz.entity.*;
import com.ementor.quiz.enums.RoleEnum;
import com.ementor.quiz.repo.*;
import jakarta.persistence.EntityManager;
import java.time.OffsetDateTime;
import java.util.*;
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

	private final QuizzesStudentsViewRepo quizzesStudentsViewRepo;

	private final QuizzesStudentsRepo quizzesStudentsRepo;

	private final UsersAnswersRepo usersAnswersRepo;

	private static final String STUDENT_FORBIDDEN = "Student does not have access to this test.";

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
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR, RoleEnum.STUDENT);

		User currentUser = securityService.getCurrentUser();

		log.info("[USER-ID: {}] Getting quiz.", currentUser.getUserId());
		Quiz quiz = getQuizById(quizId);
		List<ChapterDTO> chapterDTOList = buildChaptersDtoList(quiz.getChapters(), currentUser);
		List<QuestionDTO> questionDTOList = buildQuestionDtoList(quiz.getQuestions(), currentUser);

		log.info("[USER-ID: {}] Got quiz.", currentUser.getUserId());

		QuizDTO quizDTO = QuizDTO.builder()
			.id(quiz.getId())
			.title(quiz.getTitle())
			.description(quiz.getDescription())
			.componentType(quiz.getComponentType())
			.difficultyLevel(quiz.getDifficultyLevel())
			.maxTime(quiz.getMaxTime())
			.chapters(chapterDTOList)
			.createdBy(quiz.getCreatedBy())
			.build();

		if (currentUser.getRole()
			.equals(RoleEnum.STUDENT)) {
			return quizDTO;
		}

		quizDTO.setQuestions(questionDTOList);

		return quizDTO;
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

	public void assign(List<AssignQuizDTO> dtoList) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);

		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();
		log.info("[USER-ID: {}] Assigning quizzes.", currentUserId);

		List<QuizStudent> quizStudents = dtoList.stream()
			.map(assignQuizDTO -> {
				QuizStudent quizStudentToAssign = QuizStudent.builder()
					.quizId(assignQuizDTO.getQuizId())
					.userId(assignQuizDTO.getUserId())
					.startAfter(assignQuizDTO.getStartAfter())
					.createdBy(currentUserId)
					.build();
				quizStudentToAssign.setExpires(assignQuizDTO.getExpires());
				return quizStudentToAssign;
			})
			.toList();

		quizzesStudentsRepo.saveAll(quizStudents);

		log.info("[USER-ID: {}] Assigned quizzes.", currentUserId);
	}

	public PaginatedResponse<QuizzesStudentsView> getPaginatedQuizzesStudents(PaginatedRequest request) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR, RoleEnum.STUDENT);
		User user = securityService.getCurrentUser();
		String filterCriteria = "userId";

		log.info("[USER-ID:{}] Getting quizzes list - page {}", user.getUserId(), request.getPage());

		final PaginatedRequestSpecification<QuizzesStudentsView> spec = PaginatedRequestSpecificationUtils
			.genericSpecification(request.bind(QuizzesStudentsView.class), false, QuizzesStudentsView.class);
		if (user.getRole()
			.equals(RoleEnum.STUDENT)) {
			PaginationUtils.addFilterCriteria(request, filterCriteria, user.getUserId());
		}
		Page<QuizzesStudentsView> findAll = quizzesStudentsViewRepo.findAll(spec,
				ServiceUtils.convertToPageRequest(request));
		PaginatedResponse<QuizzesStudentsView> paginatedResponse = new PaginatedResponse<>();
		if (user.getRole()
			.equals(RoleEnum.STUDENT)) {
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

	public QuizDTO startQuiz(UUID quizStudentId) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.STUDENT);

		User currentUser = securityService.getCurrentUser();

		log.info("[USER-ID: {}] Starting quiz.", currentUser.getUserId());

		QuizStudent quizStudent = getQuizStudentById(quizStudentId);

		// TODO implement start_after verification for start and submit

		if (quizStudent.getStartAt() != null) {
			log.error("Student does not have access to this test.It already has been started.");
			throw new EmentorApiError("Student does not have access to this test.It already has been started.", 401);
		}

		Quiz quiz = getQuizById(quizStudent.getQuizId());
		List<ChapterDTO> chapterDTOList = buildChaptersDtoList(quiz.getChapters(), currentUser);
		List<QuestionDTO> questionDTOList = buildQuestionDtoList(quiz.getQuestions(), currentUser);

		quizStudent.setStartAt(OffsetDateTime.now());
		OffsetDateTime timeShouldEnd = OffsetDateTime.now()
			.plusMinutes(quiz.getMaxTime());

		// TODO implement base64 encoding for http transfer

		QuizDTO quizDTO = QuizDTO.builder()
			.id(quiz.getId())
			.title(quiz.getTitle())
			.description(quiz.getDescription())
			.componentType(quiz.getComponentType())
			.difficultyLevel(quiz.getDifficultyLevel())
			.maxTime(quiz.getMaxTime())
			.chapters(chapterDTOList)
			.questions(questionDTOList)
			.endTime(timeShouldEnd)
			.createdBy(quiz.getCreatedBy())
			.build();

		quizStudent.setEndTime(timeShouldEnd.plusMinutes(3));
		quizzesStudentsRepo.save(quizStudent);

		log.info("[USER-ID: {}] Started quiz.", currentUser.getUserId());

		return quizDTO;
	}

	public SubmitQuizDTO submit(SubmitQuizDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.STUDENT);

		// TODO refactor this method

		// TODO implement start_after verification for start and submit

		User currentUser = securityService.getCurrentUser();

		log.info("[USER-ID: {}] Submitting quiz.", currentUser.getUserId());

		QuizStudent quizStudent = getQuizStudentById(dto.getQuizStudentId());

		if (quizStudent.getStartAt() == null) {
			log.error("Student does not have access to this test. It never has been started.");
			throw new EmentorApiError("Student does not have access to this test. It never has been started.", 404);
		}
		quizStudent.setEndedTime(OffsetDateTime.now());

		Quiz quiz = getQuizById(quizStudent.getQuizId());
		List<UserAnswer> usersAnswers = getUsersAnswersAndCorrect(dto.getSubmitedQuestionAnswers(), quiz, currentUser);

		Integer correctCount = countCorrectAnswers(usersAnswers);
		quizStudent.setCorrectAnswers(correctCount);

		quizzesStudentsRepo.save(quizStudent);
		usersAnswersRepo.saveAll(usersAnswers);

		List<ChapterDTO> chapterDTOList = buildChaptersDtoList(quiz.getChapters(), currentUser);
		List<QuestionDTO> questionDTOList = buildQuestionDtoList(quiz.getQuestions(), currentUser);
		OffsetDateTime timeShouldEnd = OffsetDateTime.now()
			.plusMinutes(quiz.getMaxTime());

		List<SubmitedQuestionAnswer> correctAnswers = usersAnswers.stream()
			.map(userAnswer -> SubmitedQuestionAnswer.builder()
				.questionId(userAnswer.getQuestionId())
				.answer(userAnswer.getCorrectAnswer())
				.build())
			.toList();

		QuizDTO quizDTO = QuizDTO.builder()
			.id(quiz.getId())
			.title(quiz.getTitle())
			.description(quiz.getDescription())
			.componentType(quiz.getComponentType())
			.difficultyLevel(quiz.getDifficultyLevel())
			.maxTime(quiz.getMaxTime())
			.chapters(chapterDTOList)
			.questions(questionDTOList)
			.endTime(timeShouldEnd)
			.createdBy(quiz.getCreatedBy())
			.build();

		log.info("[USER-ID: {}] Submitted quiz.", currentUser.getUserId());

		return SubmitQuizDTO.builder()
			.quiz(quizDTO)
			.startedAt(quizStudent.getStartAt())
			.enddedAt(quizStudent.getEndedTime())
			.correctCount(correctCount)
			.correctAnswers(correctAnswers)
			.build();

	}

	private List<UserAnswer> getUsersAnswersAndCorrect(List<SubmitedQuestionAnswer> submitedQuestionAnswers,
			Quiz quiz,
			User user) {
		Map<UUID, Question> questionMap = new HashMap<>();

		for (Question question : quiz.getQuestions()) {
			questionMap.put(question.getId(), question);
		}

		return submitedQuestionAnswers.stream()
			.map(submitedQuestionAnswer -> {

				Question question = questionMap.get(submitedQuestionAnswer.getQuestionId());

				return UserAnswer.builder()
					.userId(user.getUserId())
					.quizId(quiz.getId())
					.questionId(question.getId())
					.answer(submitedQuestionAnswer.getAnswer())
					.correctAnswer(question.getCorrectAnswer())
					.build();
			})
			.toList();
	}

	private Integer countCorrectAnswers(List<UserAnswer> usersAnswers) {
		int correct = 0;
		for (UserAnswer userAnswer : usersAnswers) {
			if (userAnswer.getAnswer()
				.shortValue() == userAnswer.getCorrectAnswer()
					.shortValue()) {
				correct += 1;
			}
		}
		return correct;
	}

	public Quiz getQuizById(UUID quizId) {
		return quizzesRepo.findById(quizId)
			.orElseThrow(() -> new EmentorApiError("Quiz not found", 404));
	}

	public QuizStudent getQuizStudentById(UUID quizId) {
		return quizzesStudentsRepo.findById(quizId)
			.orElseThrow(() -> new EmentorApiError("Quiz not assigned to this student", 404));
	}
}
