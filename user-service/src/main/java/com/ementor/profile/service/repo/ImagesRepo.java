/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.repo;

import com.ementor.profile.service.entity.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ImagesRepo extends JpaRepository<Image, UUID>, JpaSpecificationExecutor<Image> {
}