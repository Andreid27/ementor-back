/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.redis.entity.StoredRedisToken;
import com.ementor.profile.service.core.redis.services.StoredRedisTokenService;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.ProfilePrerequrireDTO;
import com.ementor.profile.service.dto.StudentProfileDTO;
import com.ementor.profile.service.entity.ProfilePicture;
import com.ementor.profile.service.entity.StudentProfile;
import com.ementor.profile.service.entity.User;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.StudentProfilesRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentProfileService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ProfilePictureService profilePictureService;

	private final AddressService addressService;

	private final UniversityService universityService;

	private final SpecialityService specialityService;

	private final LocationService locationService;

	private final StoredRedisTokenService storedRedisTokenService;

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

		studentProfile = studentProfilesRepo.save(studentProfile);

		sendProfilePictureThumbnail();

		log.info("[USER-ID: {}] Created  student profile.", currentUserId);
	}

	private StudentProfile saveStudentProfile(StudentProfileDTO dto,
			UUID currentUserId) {
		return StudentProfile.builder()
			.userId(currentUserId)
			.picture(profilePictureService.getImageById(dto.getPictureId()))
			.desiredExamDate(dto.getDesiredExamDate())
			.desiredUniversity(universityService.getUniversity(dto.getUniversityId()))
			.desiredSpeciality(specialityService.getSpeciality(dto.getSpecialityId()))
			.school(dto.getSchool())
			.schoolGrade(dto.getSchoolGrade())
			.address(addressService.createAddress(dto.getAddress()))
			.build();
	}

	private void sendProfilePictureThumbnail() {
		User user = securityService.getCurrentUser();
		ProfilePicture profilePicture = profilePictureService.getImageByUserId(user.getUserId());
		byte[] reducedFilePicture = null;
		try {
			reducedFilePicture = profilePictureService.getPreparedIamge(profilePicture.getFileData(), 100, 100);
		} catch (IOException e) {
			throw new EmentorApiError("Could not get bytes of data. Either cannot save or cannot compress.", 404);
		}
		StoredRedisToken storedRedisToken = storedRedisTokenService.getStoredRedisToken(user.getUsername())
			.orElseThrow(() -> new EmentorApiError("Token not found. Check if it is last token or valid token."));
		if (storedRedisToken.getToken() == null) {
			log.error("StoredRedisToken error: Token not found for user [{}]", user.getUserId());
			return;
		}

		sendPostWithFile(reducedFilePicture, storedRedisToken, profilePicture.getFileName());

	}

	public void sendPostWithFile(byte[] reducedFilePicture,
			StoredRedisToken storedRedisToken,
			String originalFilename) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.setBearerAuth(storedRedisToken.getToken());

		ByteArrayResource resource = new ByteArrayResource(reducedFilePicture) {
			@Override
			public String getFilename() {
				return "thumb_" + originalFilename;
			}
		};

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", resource);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		String serverUrl = "https://api.e-mentor.ro/service1/profile-image/upload"; // Replace
																					// this
																					// with
																					// your
																					// receiving
																					// API
																					// URL
		ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			String isUploaded = response.getBody();
			// process the boolean response here
			log.info("Post success, {}", isUploaded);

			// TODO continue here validating UUID.
		} else {
			// handle the error here
			log.error(response.getStatusCode()
				.toString());
		}
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
			.picture(profilePictureService.getImageById(dto.getPictureId()))
			.desiredExamDate(dto.getDesiredExamDate())
			.desiredUniversity(universityService.getUniversity(dto.getUniversityId()))
			.desiredSpeciality(specialityService.getSpeciality(dto.getSpecialityId()))
			.school(dto.getSchool())
			.schoolGrade(dto.getSchoolGrade())
			.address(addressService.updateAddress(dto.getAddress()))
			.build();
	}

	public ProfilePrerequrireDTO getProfilePrerequire() {

		log.info("[NEW_USER] Getting profile needed data.");

		ProfilePrerequrireDTO profilePrerequrireDTO = ProfilePrerequrireDTO.builder()
			.counties(locationService.getAllByLevelCodes(List.of("COUNTY", "SECTOR")))
			.universities(universityService.getAll())
			.build();

		log.info("[NEW_USER] Got profile needed data.");
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

}
