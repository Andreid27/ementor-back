/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
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
@Table(name = "student_profiles")
public class StudentProfile extends CommonEntity {

	@Column(name = "user_id")
	private UUID userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "picture")
	private ProfilePicture picture;

	@Column(name = "desired_exam_date")
	private OffsetDateTime desiredExamDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "desired_university_id")
	private University desiredUniversity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "desired_speciality_id")
	private Speciality desiredSpeciality;

	@Column(name = "school")
	private String school;

	@Column(name = "school_grade")
	private float schoolGrade;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "professor_id")
	private ProfessorProfile professorProfile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

}
