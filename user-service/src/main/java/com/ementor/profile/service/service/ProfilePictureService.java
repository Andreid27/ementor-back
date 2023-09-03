/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.entity.ProfilePicture;
import com.ementor.profile.service.repo.ProfilePictureRepo;
import com.ementor.profile.service.utils.PictureUtils;
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
public class ProfilePictureService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ProfilePictureRepo profilePictureRepo;

	private final UserServiceRestService userServiceRest;

	@Transactional
	public UUID saveProfilePicture(MultipartFile file) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Saving  profilePicture.", currentUserId);

		boolean updatePicture = false;

		Optional<ProfilePicture> databaseProfilePicture = profilePictureRepo.findByCreatedBy(currentUserId);
		ProfilePicture profilePicture = null;

		if (databaseProfilePicture.isPresent()) {
			profilePicture = databaseProfilePicture.get();
			updatePicture = true;
		} else {
			profilePicture = new ProfilePicture();
		}

		try {
			byte[] compressedImage = PictureUtils.getPreparedIamge(file.getBytes(), 1920, 1920);
			profilePicture.setFileData(compressedImage);
			profilePicture.setSize(compressedImage.length);
		} catch (IOException ioException) {
			throw new EmentorApiError("Could not get bytes of data. Either cannot save or cannot compress.", 404);
		}
		profilePicture.setCreatedBy(currentUserId);
		profilePicture.setFileName(file.getOriginalFilename());
		profilePicture.setFileType(file.getContentType());

		profilePicture = profilePictureRepo.save(profilePicture);

		if (updatePicture) {
			userServiceRest.sendProfilePictureThumbnail();
		}

		log.info("[USER-ID: {}] Saved profilePicture.", currentUserId);

		return profilePicture.getId();
	}

	public ResponseEntity<Resource> download(final UUID fileId) {
		final UUID currentUserId = securityService.getCurrentUser()
			.getUserId();
		log.info("[USER: {}] Downloading profilePicture file with id {}...", currentUserId, fileId);

		ProfilePicture profilePicture = getImageById(fileId);

		log.info("[USER: {}] Downloaded profilePicture file with id {}.", currentUserId, fileId);

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(profilePicture.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + profilePicture.getFileName() + "\"")
			.body(new ByteArrayResource(profilePicture.getFileData()));
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