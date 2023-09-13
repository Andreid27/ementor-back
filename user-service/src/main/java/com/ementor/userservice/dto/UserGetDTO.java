/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.dto;

import com.ementor.userservice.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetDTO {
	private String email;

	private String phone;

	private String firstName;

	private String lastName;

	private RoleEnum role;

	private Boolean active;

	private Boolean disabled;

	private Boolean hasProfile;
}
