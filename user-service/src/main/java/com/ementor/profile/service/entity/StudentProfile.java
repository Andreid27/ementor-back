/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student_profile")
public class StudentProfile extends CommonEntity {

	@Column(name = "user_id")
	private UUID userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "picture")
	private Image picture;

	@Column(name = "desired_exam_date")
	protected OffsetDateTime desiredExamDate;

	@Column(name = "desired_university")
	private String desiredUniversity;
	// TODO de adaugat ca enum.

	@Column(name = "school")
	private String school;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private ProfessorProfile professorProfile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

}
