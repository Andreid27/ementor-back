/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import com.ementor.profile.service.enums.RoleEnum;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

	private UUID userId;

	private String email;

	private String firstName;

	private String lastName;

	private String phone;

	private RoleEnum role;

	private Boolean active;

	private Boolean disabled;

	private Boolean hasProfile;
}
