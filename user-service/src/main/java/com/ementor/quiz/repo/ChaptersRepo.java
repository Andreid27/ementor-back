package com.ementor.quiz.repo;

import com.ementor.quiz.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface ChaptersRepo extends JpaRepository<Chapter, UUID>, JpaSpecificationExecutor<Chapter> {
}
