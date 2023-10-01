/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "locations_levels")
public class LocationLevel extends CommonEntity {

	private static final long serialVersionUID = 8758493621589040812L;

	@NotNull
	@Column(name = "level_name")
	private String levelName;

	@NotNull
	@Column(name = "abbreviation")
	private String abbreviation;

	@NotNull
	@Column(name = "abbreviation_long")
	private String abbreviationLong;

	private String code;

	public LocationLevel() {
		super();
	}

	public LocationLevel(
			String levelName,
			String abbreviation,
			String abbreviationLong,
			String code) {
		this.levelName = levelName;
		this.abbreviation = abbreviation;
		this.abbreviationLong = abbreviationLong;
		this.code = code;
	}

	public LocationLevel(
			UUID id,
			OffsetDateTime creation,
			OffsetDateTime expires,
			OffsetDateTime modified,
			String levelName,
			String abbreviation,
			String abbreviationLong,
			String code) {
		super(id, creation, expires, modified);
		this.levelName = levelName;
		this.abbreviation = abbreviation;
		this.abbreviationLong = abbreviationLong;
		this.code = code;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbbreviationLong() {
		return abbreviationLong;
	}

	public void setAbbreviationLong(String abbreviationLong) {
		this.abbreviationLong = abbreviationLong;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
