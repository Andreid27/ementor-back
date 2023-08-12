/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.entity.StudentProfile;
import com.ementor.profile.service.repo.StudentProfilesRepo;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentProfileService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final StudentProfilesRepo studentProfilesRepo;

	public String localDateTimeLogger() {
		return String.valueOf(OffsetDateTime.now());
	}

	public void createStudentProfile(StudentProfileDTO dto) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  student profile.", currentUserId);
		StudentProfile studentProfile = new StudentProfile();

		studentProfile = saveStudentProfile(studentProfile, dto, currentUserId);

		log.info("[USER-ID: {}] Created  student profile.", currentUserId);
	}

	private StudentProfile saveStudentProfile(StudentProfile studentProfile,
			StudentProfileDTO dto,
			UUID currentUserId) {
		return StudentProfile.builder()
			.userId(currentUserId)
			// TODO continue here
			.build();
	}

}
