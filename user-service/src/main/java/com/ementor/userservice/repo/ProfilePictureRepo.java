/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.repo;

import com.ementor.userservice.entity.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface ProfilePictureRepo extends JpaRepository<ProfilePicture, UUID>, JpaSpecificationExecutor<ProfilePicture> {
	@Transactional
	Optional<ProfilePicture> findByCreatedBy(UUID userId);
}