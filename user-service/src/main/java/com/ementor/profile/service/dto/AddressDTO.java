/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import jakarta.validation.constraints.NotBlank;
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
public class AddressDTO {

	private UUID id;

	@NotNull(message = "Address county field is required")
	private UUID countyId;

	private String countyValue;

	@NotBlank(message = "Address city field is required")
	@Size(min = 1, max = 100, message = "Address city must contain between 1 and 100 characters!")
	private String city;

	@NotBlank(message = "Address street field is required")
	@Size(min = 1, max = 200, message = "Address street must contain between 2 and 200 characters!")
	private String street;

	@NotBlank(message = "Address number field is required")
	@Size(min = 1, max = 10, message = "Address number must contain between 1 and 10 characters!")
	private String number;

	private String block;

	private String staircase;

	private Integer apartment;
}
