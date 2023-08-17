/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.*;
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
@Table(name = "professor_profile")
public class ProfessorProfile extends CommonEntity {

	@Column(name = "user_id")
	private UUID userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "picture")
	private ProfilePicture picture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "university_id")
	private University university;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id")
	private Speciality speciality;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "about")
	private String about;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

}
