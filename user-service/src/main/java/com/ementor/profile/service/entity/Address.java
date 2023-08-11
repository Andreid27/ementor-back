/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "office_addresses")
public class Address extends CommonEntity {
	@Serial
	private static final long serialVersionUID = 6565579143327517074L;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "county_id")
	private Location county;

	@Column(name = "city", length = 100)
	@NotBlank(message = "City name value cannot be null or blank")
	@Size(min = 1, max = 100)
	private String city;

	@Column(name = "street", length = 200)
	@NotBlank(message = "Street name value cannot be null or blank")
	@Size(min = 1, max = 200)
	private String street;

	@Column(name = "no", length = 10)
	@NotBlank(message = "Number value cannot be null or blank")
	@Size(min = 1, max = 10)
	private String number;

	@Column(name = "block", length = 50)
	@Size(max = 50)
	private String block;

	@Column(name = "staircase")
	private String staircase;

	@Column(name = "apartment")
	private Integer apartment;
}
