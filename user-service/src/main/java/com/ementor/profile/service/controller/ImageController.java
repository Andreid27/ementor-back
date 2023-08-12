/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.controller;

import com.ementor.profile.service.entity.Image;
import com.ementor.profile.service.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/profile-image")
@RequiredArgsConstructor
public class ImageController {
	private final ImageService service;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
	@Operation(summary = "Upload a new image")
	@ApiResponses(
		value = {@ApiResponse(responseCode = "200", description = "Request successful"),
				@ApiResponse(responseCode = "400", description = "Invalid request")})
	public void upload(@RequestPart(name = "file") MultipartFile file) {
		service.saveImage(file);
	}

	@GetMapping(
			value = "/show/{fileName}")
	@ApiResponses(
			value = {@ApiResponse(responseCode = "200", description = "Request successful",
					content = @Content(
					mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
					schema = @Schema(type = "string", format = "binary"))),
					@ApiResponse(responseCode = "400", description = "Invalid request")}
	)
	public ResponseEntity<byte[]> getImage(@PathVariable UUID fileName) throws Exception {
		Image image = service.getImageByName(fileName);
		//TODO continue here download
		return ResponseEntity.ok().contentType(MediaType.valueOf(image.getFileType())).body(image.getFileData());
	}
}
