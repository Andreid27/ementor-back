/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.repo;

import com.ementor.profile.service.entity.ProfilePicture;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

public interface ProfilePictureRepo extends JpaRepository<ProfilePicture, UUID>, JpaSpecificationExecutor<ProfilePicture> {
	@Transactional
	Optional<ProfilePicture> findByCreatedBy(UUID userId);
}