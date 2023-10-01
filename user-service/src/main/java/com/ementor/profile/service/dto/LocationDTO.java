/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {
	private UUID id;
	private String name;
}
