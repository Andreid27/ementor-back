/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.ProfilePrerequrireDTO;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.entity.Image;
import com.ementor.profile.service.entity.StudentProfile;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.StudentProfilesRepo;
import java.util.List;
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

	private final LocationService locationService;

	private final StudentProfilesRepo studentProfilesRepo;

	// TODO add paginated

	public StudentProfileDTO getUserProfile() {
		securityService.hasAnyRole(RoleEnum.STUDENT);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting current student profile.", currentUserId);

		StudentProfile studentProfile = getStudentProfileByUserId(currentUserId);
		StudentProfileDTO studentProfileDTO = buildStudentProfileDto(studentProfile);
		studentProfileDTO.setId(studentProfile.getId());

		log.info("[USER-ID: {}] Got current student profile.", currentUserId);

		return studentProfileDTO;
	}

	public StudentProfileDTO get(UUID studentProfileId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting student profile with id {}.", currentUserId, studentProfileId);

		StudentProfile studentProfile = getStudentProfileById(studentProfileId);
		StudentProfileDTO studentProfileDTO = buildStudentProfileDto(studentProfile);
		studentProfileDTO.setId(studentProfile.getId());

		log.info("[USER-ID: {}] Got student profile with id {}.", currentUserId, studentProfileId);

		return studentProfileDTO;
	}

	public StudentProfileDTO buildStudentProfileDto(StudentProfile studentProfile) {
		return StudentProfileDTO.builder()
			.pictureId(studentProfile.getPicture()
				.getId())
			.desiredExamDate(studentProfile.getDesiredExamDate())
			.universityId(studentProfile.getDesiredUniversity()
				.getId())
			.specialityId(studentProfile.getDesiredSpeciality()
				.getId())
			.school(studentProfile.getSchool())
			.schoolGrade(studentProfile.getSchoolGrade())
			.address(addressService.buildAddressDto(studentProfile.getAddress()))
			.build();
	}

	public void createStudentProfile(StudentProfileDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR, RoleEnum.STUDENT);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  student profile.", currentUserId);

		StudentProfile studentProfile = saveStudentProfile(dto, currentUserId);

		studentProfilesRepo.save(studentProfile);

		log.info("[USER-ID: {}] Created  student profile.", currentUserId);
	}

	private StudentProfile saveStudentProfile(StudentProfileDTO dto,
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

	public void updateStudentProfile(StudentProfileDTO dto) {
		securityService.hasAnyRole(RoleEnum.STUDENT);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Updating  student profile.", currentUserId);

		StudentProfile studentProfile = buildUpdateStudentProfile(dto, currentUserId);

		studentProfilesRepo.save(studentProfile);

		log.info("[USER-ID: {}] Updated  student profile.", currentUserId);
	}

	private StudentProfile buildUpdateStudentProfile(StudentProfileDTO dto,
			UUID currentUserId) {
		return StudentProfile.builder()
			.userId(currentUserId)
			.picture(imageService.getImageById(dto.getPictureId()))
			.desiredExamDate(dto.getDesiredExamDate())
			.desiredUniversity(universityService.getUniversity(dto.getUniversityId()))
			.desiredSpeciality(specialityService.getSpeciality(dto.getSpecialityId()))
			.school(dto.getSchool())
			.schoolGrade(dto.getSchoolGrade())
			.address(addressService.updateAddress(dto.getAddress()))
			.build();
	}

	public ProfilePrerequrireDTO getProfilePrerequire() {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting profile needed data.", currentUserId);

		ProfilePrerequrireDTO profilePrerequrireDTO = ProfilePrerequrireDTO.builder()
			.counties(locationService.getAllByLevelCodes(List.of("COUNTY", "SECTOR")))
			.universities(universityService.getAll())
			.build();
		log.info("[USER-ID: {}] Got profile needed data.", currentUserId);
		return profilePrerequrireDTO;
	}

	public StudentProfile getStudentProfileById(UUID studentProfileId) {
		return studentProfilesRepo.findById(studentProfileId)
			.orElseThrow(() -> new EmentorApiError("Student profile not found"));
	}

	public StudentProfile getStudentProfileByUserId(UUID userId) {
		return studentProfilesRepo.findByUserId(userId)
			.orElseThrow(() -> new EmentorApiError("Student profile not found"));
	}

	public Image getProfileImageByUserId(UUID userId) {
		return imageService.getImageById(getStudentProfileByUserId(userId).getPicture()
			.getId());
	}

}
