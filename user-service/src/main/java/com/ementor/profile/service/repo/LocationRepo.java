/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.repo;

import com.ementor.profile.service.entity.Location;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepo extends JpaRepository<Location, UUID> {

	@Query("select l from Location l where l.locationLevel.code in (:codes)")
	List<Location> findAllByCodes(@Param("codes") List<String> codes);

	@Query("select l.id from Location l where l.shortCode = :shortcode and l.locationLevel.code in (:codes)")
	UUID findByCode(@Param("shortcode") String codeLocation,
			@Param("codes") List<String> codeLevel);
}
