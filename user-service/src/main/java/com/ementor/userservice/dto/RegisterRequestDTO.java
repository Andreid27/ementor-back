/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

	@NotBlank(message = "First Name is field required")
	@Size(min = 3, max = 100, message = "First Name must contain between 3 and 100 characters!")
	private String firstName;

	@NotBlank(message = "Last Name is field required")
	@Size(min = 3, max = 100, message = "Last Name must contain between 3 and 100 characters!")
	private String lastName;

	@NotBlank(message = "Email is field required")
	@Size(min = 3, max = 100, message = "Email must contain between 3 and 100 characters!")
	private String email;

	@NotBlank(message = "Password is field required")
	@Size(min = 6, max = 96, message = "Password must contain between 6 and 96 characters!")
	private String password;

	@NotBlank(message = "Phone is field required")
	@Size(min = 9, max = 16, message = "Phone must contain between 9 and 16 characters!")
	private String phone;
}
