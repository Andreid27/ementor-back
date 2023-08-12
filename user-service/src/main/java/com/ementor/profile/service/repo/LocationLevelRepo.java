/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.repo;

import com.ementor.profile.service.entity.LocationLevel;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationLevelRepo extends JpaRepository<LocationLevel, UUID> {
}
