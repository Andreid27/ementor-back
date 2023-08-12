/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversitySpecialitiesSendDTO {
	@NotNull(message = "Speciality/Speciality id field is required")
	private UUID speciality;

	@NotNull(message = "Difficulty field is required")
	private short difficulty;

	private short about;
}
