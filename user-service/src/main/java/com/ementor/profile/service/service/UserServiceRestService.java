/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import static com.ementor.profile.service.utils.PictureUtils.getPreparedIamge;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.redis.entity.StoredRedisToken;
import com.ementor.profile.service.core.redis.services.StoredRedisTokenService;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.entity.ProfilePicture;
import com.ementor.profile.service.entity.User;
import com.ementor.profile.service.repo.ProfilePictureRepo;
import com.ementor.profile.service.repo.StudentProfilesRepo;
import com.ementor.profile.service.utils.ConstantUtils;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceRestService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final SecurityService securityService;

	private final StoredRedisTokenService storedRedisTokenService;

	private final ProfilePictureRepo profilePictureRepo;

	private final StudentProfilesRepo studentProfilesRepo;

	public void sendProfilePictureThumbnail() {
		User user = securityService.getCurrentUser();
		ProfilePicture profilePicture = getImageByUserId(user.getUserId());
		byte[] reducedFilePicture = null;
		try {
			reducedFilePicture = getPreparedIamge(profilePicture.getFileData(), 100, 100);
		} catch (IOException e) {
			throw new EmentorApiError("Could not get bytes of data. Either cannot save or cannot compress.", 404);
		}
		StoredRedisToken storedRedisToken = storedRedisTokenService.getStoredRedisToken(user.getUsername())
			.orElseThrow(() -> new EmentorApiError("Token not found. Check if it is last token or valid token."));
		if (storedRedisToken.getToken() == null) {
			log.error("StoredRedisToken error: Token not found for user [{}]", user.getUserId());
			return;
		}

		Boolean hasUploadedThumbnail = sendPostWithFile(reducedFilePicture, storedRedisToken, profilePicture.getFileName());
		Boolean hasProfileCompleted = studentProfilesRepo.findByUserId(user.getUserId())
			.isPresent();

		if (hasUploadedThumbnail && hasProfileCompleted) {
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(storedRedisToken.getToken());

			HttpEntity<?> requestEntity = new HttpEntity<>(headers);

			RestTemplate restTemplate = new RestTemplate();
			String serverUrl = ConstantUtils.USER_SERVICE_PROD_URL + "/profile-data/profile-completed/true";

			ResponseEntity<Boolean> response = restTemplate.exchange(serverUrl, HttpMethod.GET, requestEntity,
					Boolean.class);

			Boolean responseBody = response.getBody();
			log.info("User {} completed the successful: {}", user.getEmail(), responseBody);
		}
	}

	public Boolean sendPostWithFile(byte[] reducedFilePicture,
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
		String serverUrl = ConstantUtils.USER_SERVICE_PROD_URL + "/profile-data/upload";
		ResponseEntity<String> response = restTemplate.postForEntity(serverUrl, requestEntity, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			String isUploaded = response.getBody();
			try {
				if (isUploaded == null) {
					throw new IllegalArgumentException();
				}
				isUploaded = isUploaded.replace("\"", "");
				UUID uuid = UUID.fromString(isUploaded);
				log.info("Post success, image uploaded with id: {}", uuid);
				return true;
			} catch (IllegalArgumentException e) {
				throw new EmentorApiError("The upload was not successful, UUID not found");
			}
		} else {
			log.error(response.getStatusCode()
				.toString());
			return false;
		}
	}

	public ProfilePicture getImageByUserId(UUID userId) {
		return profilePictureRepo.findByCreatedBy(userId)
			.orElseThrow(() -> new EmentorApiError("ProfilePicture not found"));
	}
}
