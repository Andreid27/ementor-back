/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import jakarta.persistence.*;
import java.util.UUID;

public class StudentProfileDTO {
	private UUID userId;

	private UUID pictureId;

	private UUID universityId;

	private UUID specialityId;

	private String fullName;

	private String about;

	private AddressDTO address;
}
