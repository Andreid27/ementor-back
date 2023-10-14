/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.repo;

import com.ementor.quiz.entity.QuizzesStudentsView;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuizzesStudentsViewRepo
		extends
			JpaRepository<QuizzesStudentsView, UUID>,
			JpaSpecificationExecutor<QuizzesStudentsView> {
}
