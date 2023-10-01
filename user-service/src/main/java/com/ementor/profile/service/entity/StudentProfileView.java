/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_profile_view")
public class StudentProfileView implements Serializable {
	@Id
	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "desired_university_id")
	private String desiredUniversity;

	@Column(name = "desired_speciality_id")
	private String desiredSpeciality;

	@Column(name = "school")
	private String school;

	@Column(name = "professor_id")
	private String professorId;

}
