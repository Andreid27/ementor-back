/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.SpecialitySendDTO;
import com.ementor.profile.service.entity.Speciality;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.SpecialitiesRepo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialityService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final SpecialitiesRepo specialitiesRepo;

	public void createSpeciality(SpecialitySendDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  speciality {}.", currentUserId, dto.getName());

		Speciality speciality = Speciality.builder()
			.name(dto.getName())
			.studyYears(dto.getStudyYears())
			.about(dto.getAbout())
			.build();

		specialitiesRepo.save(speciality);
		log.info("[USER-ID: {}] Created  speciality {} and id {}.", currentUserId, speciality.getName(), speciality.getId());
	}

	public Speciality getSpeciality(final UUID locationId) {
		return specialitiesRepo.findById(locationId)
			.orElseThrow(() -> new EmentorApiError("Speciality not found"));
	}
}
