/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.repo;

import com.ementor.quiz.entity.QuizStudent;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface QuizzesStudentsRepo extends JpaRepository<QuizStudent, UUID>, JpaSpecificationExecutor<QuizStudent> {
	List<QuizStudent> findAllByUserIdAndQuizId(UUID userId,
			UUID quizId);

	@Query("SELECT COUNT(DISTINCT q.quizId) FROM QuizStudent q WHERE q.userId = :userId AND q.startAt IS NOT NULL")
	Integer countDistinctQuizIdByUserId(UUID userId);
}
