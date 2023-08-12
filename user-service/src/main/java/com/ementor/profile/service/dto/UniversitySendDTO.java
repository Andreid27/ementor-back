/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import com.ementor.profile.service.enums.ExamBookEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversitySendDTO {
	@NotBlank(message = "Name is field required")
	@Size(min = 2, max = 50, message = "name must contain between 2 and 50 characters!")
	private String name;

	@Valid
	private AddressSendDTO address;

	private String phone;

	private ExamBookEnum examBook;

	private List<UniversitySpecialitiesSendDTO> specialities;
}
