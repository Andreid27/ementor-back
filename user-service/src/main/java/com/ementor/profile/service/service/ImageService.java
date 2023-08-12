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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ImagesRepo imagesRepo;

	public void saveImage(MultipartFile file) {
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

		imagesRepo.save(image);

		log.info("[USER-ID: {}] Saved image.", currentUserId);
	}

	public Image getImageByName(UUID id) throws Exception {
		Image image = imagesRepo.findById(id).get();
		return image;
	}
}