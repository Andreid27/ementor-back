/* Copyright (C) 2022-2022 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.entity;

import com.ementor.profile.service.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "locations")
public class Location extends CommonEntity {

	private static final long serialVersionUID = 8658494621589040812L;

	@NotNull
	@Column(name = "location")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_level_id")
	private LocationLevel locationLevel;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Location parentId;

	@Column(name = "short_code")
	private String shortCode;

	@Column(name = "legacy_id")
	private String legacyId;

	public Location() {
		super();
	}

	public Location(
			String name,
			LocationLevel locationLevel,
			Location parentId,
			String shortCode,
			String legacyId) {
		this.name = name;
		this.locationLevel = locationLevel;
		this.parentId = parentId;
		this.shortCode = shortCode;
		this.legacyId = legacyId;
	}

	public Location(
			UUID id,
			OffsetDateTime creation,
			OffsetDateTime expires,
			OffsetDateTime modified,
			String name,
			LocationLevel locationLevel,
			Location parentId,
			String shortCode,
			String legacyId) {
		super(id, creation, expires, modified);
		this.name = name;
		this.locationLevel = locationLevel;
		this.parentId = parentId;
		this.shortCode = shortCode;
		this.legacyId = legacyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationLevel getLocationLevel() {
		return locationLevel;
	}

	public void setLocationLevel(LocationLevel locationLevel) {
		this.locationLevel = locationLevel;
	}

	public Location getParentId() {
		return parentId;
	}

	public void setParentId(Location parentId) {
		this.parentId = parentId;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getLegacyId() {
		return legacyId;
	}

	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
}
