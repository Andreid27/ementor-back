/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.dto;

import com.ementor.profile.service.enums.SchoolDomainEnum;
import com.ementor.profile.service.enums.SchoolSpecialityEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfileDTO {

	private UUID id;

	private UUID userId;

	private UUID pictureId;

	@NotNull(message = "University is field required")
	private UUID universityId;

	private String universityValue;

	@NotNull(message = "University speciality is field required")
	private UUID specialityId;

	private String specialityValue;

	@NotNull(message = "Disired examen date is field required")
	private OffsetDateTime desiredExamDate;

	@Size(max = 80, message = "School name be maximum 80 characters!")
	private String school;

	@Size(max = 50, message = "School domain be maximum 50 characters!")
	private SchoolDomainEnum schoolDomain;

	@Size(max = 30, message = "School name be maximum 50 characters!")
	private SchoolSpecialityEnum schoolSpeciality;

	private float schoolGrade;

	private String fullName;
	@Size(max = 1000, message = "About must be maximum 1000 characters!")
	private String about;

	@Valid
	private AddressDTO address;
}
