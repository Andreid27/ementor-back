/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.entity.Image;
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

	private final ImageService imageService;

	private final AddressService addressService;

	private final UniversityService universityService;

	private final SpecialityService specialityService;

	private final StudentProfilesRepo studentProfilesRepo;

	public String localDateTimeLogger() {
		return String.valueOf(OffsetDateTime.now());
	}

	public void createStudentProfile(StudentProfileDTO dto) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  student profile.", currentUserId);

		StudentProfile studentProfile = saveStudentProfile(dto, currentUserId);

		//TODO continue testing. NOT tested - never.
		studentProfilesRepo.save(studentProfile);

		log.info("[USER-ID: {}] Created  student profile.", currentUserId);
	}

	private StudentProfile saveStudentProfile(
			StudentProfileDTO dto,
			UUID currentUserId) {
		return StudentProfile.builder()
			.userId(currentUserId)
			.picture(imageService.getImageById(dto.getPictureId()))
			.desiredExamDate(dto.getDesiredExamDate())
			.desiredUniversity(universityService.getUniversity(dto.getUniversityId()))
			.desiredSpeciality(specialityService.getSpeciality(dto.getSpecialityId()))
			.school(dto.getSchool())
			.schoolGrade(dto.getSchoolGrade())
			.address(addressService.createAddress(dto.getAddress()))
			.build();
	}

	public StudentProfile getStudentProfileByUserId(UUID studentProfileId) {
		return studentProfilesRepo.findByUserId(studentProfileId)
			.orElseThrow(() -> new EmentorApiError("Student profile not found"));
	}

	public Image getProfileImageByUserId(UUID userId) {
		return imageService.getImageById(getStudentProfileByUserId(userId).getPicture()
			.getId());
	}
}
