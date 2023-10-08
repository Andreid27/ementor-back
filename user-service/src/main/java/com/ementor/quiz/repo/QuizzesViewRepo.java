/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.repo;

import com.ementor.quiz.entity.QuizzesView;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface QuizzesViewRepo extends JpaRepository<QuizzesView, UUID>, JpaSpecificationExecutor<QuizzesView> {
}
