/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "specialities")
public class Speciality extends CommonEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "study_years")
	private short studyYears;

	@Column(name = "about")
	private String about;

	// @OneToMany(fetch = FetchType.LAZY, mappedBy = "speciality")
	// private List<UniversitySpeciality> universities;
}