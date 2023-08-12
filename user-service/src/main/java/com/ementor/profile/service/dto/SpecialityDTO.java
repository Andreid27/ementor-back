/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityDTO {

	private UUID id;

	@NotBlank(message = "Name is field required")
	@Size(min = 2, max = 50, message = "name must contain between 2 and 50 characters!")
	private String name;

	@NotNull(message = "Years of study is field required")
	private short studyYears;

	@Size(max = 1000, message = "About must be maximum 1000 characters!")
	private String about;
}
