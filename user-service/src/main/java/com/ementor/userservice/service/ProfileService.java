/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

import com.ementor.userservice.core.exceptions.EmentorApiError;
import com.ementor.userservice.core.service.SecurityService;
import com.ementor.userservice.entity.ProfilePicture;
import com.ementor.userservice.entity.User;
import com.ementor.userservice.enums.RoleEnum;
import com.ementor.userservice.repo.ProfilePictureRepo;
import com.ementor.userservice.repo.UsersRepo;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ProfilePictureRepo profilePictureRepo;

	private final UserService userService;

	private final UsersRepo usersRepo;

	@Transactional
	public UUID saveProfilePicture(MultipartFile file) {
		UUID currentUserId = securityService.getCurrentUser()
			.getId();

		log.info("[USER-ID: {}] Saving  profilePicture.", currentUserId);

		Optional<ProfilePicture> databaseProfilePicture = profilePictureRepo.findByCreatedBy(currentUserId);
		ProfilePicture profilePicture = null;

		if (databaseProfilePicture.isPresent()) {
			profilePicture = databaseProfilePicture.get();
		} else {
			profilePicture = new ProfilePicture();
		}

		try {
			profilePicture.setFileData(file.getBytes());
		} catch (IOException ioException) {
			throw new EmentorApiError("Could not get bytes of data", 404);
		}
		profilePicture.setCreatedBy(currentUserId);
		profilePicture.setFileName(file.getOriginalFilename());
		profilePicture.setFileType(file.getContentType());
		profilePicture.setSize(file.getSize());

		profilePicture = profilePictureRepo.save(profilePicture);

		log.info("[USER-ID: {}] Saved profilePicture.", currentUserId);

		return profilePicture.getId();
	}

	public ResponseEntity<Resource> download(final UUID fileId) {
		final UUID currentUserId = securityService.getCurrentUser()
			.getId();
		log.info("[USER: {}] Downloading profilePicture file with id {}...", currentUserId, fileId);

		ProfilePicture profilePicture = getImageById(fileId);

		log.info("[USER: {}] Downloaded profilePicture file with id {}.", currentUserId, fileId);

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(profilePicture.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profilePicture.getFileName() + "\"")
			.body(new ByteArrayResource(profilePicture.getFileData()));
	}

	public ResponseEntity<Resource> downloadThumbnail() {
		final UUID currentUserId = securityService.getCurrentUser()
			.getId();
		log.info("[USER: {}] Downloading profilePicture ...", currentUserId);

		ProfilePicture profilePicture = getImageByUserId(currentUserId);

		log.info("[USER: {}] Downloaded profilePicture.", currentUserId);

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(profilePicture.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profilePicture.getFileName() + "\"")
			.body(new ByteArrayResource(profilePicture.getFileData()));
	}

	public Boolean completeProfile(Boolean isProfileCompleted) {
		final UUID currentUserId = securityService.getCurrentUser()
			.getId();
		log.info("[USER: {}] Completing profile.", currentUserId);
		securityService.hasAnyRole(RoleEnum.STUDENT, RoleEnum.PROFESSOR, RoleEnum.ADMIN);

		if (Boolean.FALSE.equals(isProfileCompleted)) {
			log.info("[USER: {}] An error interrupted profile saving success.", currentUserId);
			return false;
		}
		User user = userService.getUserByUserId(currentUserId);
		user.setHasProfile(true);

		usersRepo.save(user);

		log.info("[USER: {}] Completed profile.", currentUserId);
		return true;

	}

	public ProfilePicture getImageById(UUID id) {
		return profilePictureRepo.findById(id)
			.orElseThrow(() -> new EmentorApiError("ProfilePicture not found"));
	}
	public ProfilePicture getImageByUserId(UUID userId) {
		return profilePictureRepo.findByCreatedBy(userId)
			.orElseThrow(() -> new EmentorApiError("ProfilePicture not found"));
	}
}