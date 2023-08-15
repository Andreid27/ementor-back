/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversitySpecialitiesDTO {

	private UUID id;

	@Size(min = 2, max = 80, message = "Name must contain between 2 and 50 characters!")
	private String name;

	private short studyYears;

	private String specialityAbout;

	@NotNull(message = "Speciality/Speciality id field is required")
	private UUID specialityId;

	@NotNull(message = "Difficulty field is required")
	private short difficulty;

	@Size(max = 2000, message = "About must be maximum 2000 characters!")
	private String about;
}
