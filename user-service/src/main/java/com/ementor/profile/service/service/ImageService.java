/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.entity.Image;
import com.ementor.profile.service.repo.ImagesRepo;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ImagesRepo imagesRepo;

	public UUID saveImage(MultipartFile file) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Saving  image.", currentUserId);

		Image image = new Image();
		try {
			image.setFileData(file.getBytes());
		} catch (IOException ioException) {
			throw new EmentorApiError("Could not get bytes of data", 404);
		}
		image.setFileName(file.getOriginalFilename());
		image.setFileType(file.getContentType());
		image.setSize(file.getSize());

		image = imagesRepo.save(image);

		log.info("[USER-ID: {}] Saved image.", currentUserId);

		return image.getId();
	}

	public ResponseEntity<Resource> download(final UUID fileId) {
		final UUID currentUserId = securityService.getCurrentUser()
			.getUserId();
		log.info("[USER: {}] Downloading image file with id {}...", currentUserId, fileId);

		Image image = getImageById(fileId);

		log.info("[USER: {}] Downloaded image file with id {}.", currentUserId, fileId);

		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(image.getFileType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
			.body(new ByteArrayResource(image.getFileData()));
	}

	public Image getImageById(UUID id) {
		return imagesRepo.findById(id)
			.orElseThrow(() -> new EmentorApiError("Image not found"));
	}
}