/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.entity.ProfilePicture;
import com.ementor.profile.service.repo.ProfilePictureRepo;
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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfilePictureService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final ProfilePictureRepo profilePictureRepo;

	@Transactional
	public UUID saveProfilePicture(MultipartFile file) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Saving  profilePicture.", currentUserId);

		Optional<ProfilePicture> databaseProfilePicture = profilePictureRepo.findByCreatedBy(currentUserId);
		ProfilePicture profilePicture = null;

		if (databaseProfilePicture.isPresent()) {
			profilePicture = databaseProfilePicture.get();
		} else {
			profilePicture = new ProfilePicture();
		}

		try {
			byte[] compressedImage = getPreparedIamge(file.getBytes());
			profilePicture.setFileData(compressedImage);
			profilePicture.setSize(compressedImage.length);
		} catch (IOException ioException) {
			throw new EmentorApiError("Could not get bytes of data. Either cannot save or cannot compress.", 404);
		}
		profilePicture.setCreatedBy(currentUserId);
		profilePicture.setFileName(file.getOriginalFilename());
		profilePicture.setFileType(file.getContentType());

		profilePicture = profilePictureRepo.save(profilePicture);

		log.info("[USER-ID: {}] Saved profilePicture.", currentUserId);

		return profilePicture.getId();
	}

	private byte[] getPreparedIamge(byte[] originalBytes) throws IOException {
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalBytes));
		originalImage = getResizedImage(originalImage, 1920, 1920);
		return compressImage(originalImage);
	}

	private BufferedImage getResizedImage(BufferedImage originalImage,Integer desiredWidth, Integer desiredHeight) {
		int originalWidth = originalImage.getWidth();
		int originalHeight = originalImage.getHeight();

		if (originalWidth > desiredWidth || originalHeight > desiredHeight) {
			Dimension boundary = new Dimension(desiredWidth, desiredHeight);
			Dimension newSize = getScaledDimension(new Dimension(originalWidth, originalHeight), boundary);
			originalImage = resizeImage(originalImage, newSize.width, newSize.height);
		}
		return originalImage;
	}

	public Dimension getScaledDimension(Dimension imgSize,
			Dimension boundary) {
		double widthRatio = boundary.getWidth() / imgSize.getWidth();
		double heightRatio = boundary.getHeight() / imgSize.getHeight();
		double ratio = Math.min(widthRatio, heightRatio);

		return new Dimension((int) (imgSize.width * ratio), (int) (imgSize.height * ratio));
	}

	public BufferedImage resizeImage(BufferedImage originalImage,
			int targetWidth,
			int targetHeight) {
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();

		return resizedImage;
	}

	private byte[] compressImage(BufferedImage originalImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();

		if (imageInByte.length > 1024 * 1024) { // If size is larger than 1MB
			float compressionQuality = 0.5f; // Change this to adjust the
												// quality of the image
			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = writers.next();

			ByteArrayOutputStream compressedBaos = new ByteArrayOutputStream();
			ImageOutputStream ios = ImageIO.createImageOutputStream(compressedBaos);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compressionQuality);
			writer.write(null, new IIOImage(originalImage, null, null), param);

			ios.flush();
			ios.close();
			writer.dispose();
			compressedBaos.flush();
			imageInByte = compressedBaos.toByteArray();
			compressedBaos.close();
		}

		return imageInByte;
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
}