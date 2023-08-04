/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.dto;

import com.ementor.core.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDTO {
	private String email;

	private String firstName;

	private String lastName;

	private RoleEnum role;
}
