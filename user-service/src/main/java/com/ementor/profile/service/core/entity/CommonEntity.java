/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Data;
@Data
@MappedSuperclass
public class CommonEntity implements Serializable {
	private static final long serialVersionUID = 2747996778508770388L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected UUID id;

	@Column(name = "creation")
	protected OffsetDateTime creation;

	@Column(name = "expires")
	protected OffsetDateTime expires;

	@Column(name = "modified")
	protected OffsetDateTime modified;

	@PrePersist
	public void prePersist() {
		if (this.creation == null) {
			this.creation = OffsetDateTime.now();
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.modified = OffsetDateTime.now();
	}

	public CommonEntity(
			UUID id,
			OffsetDateTime creation,
			OffsetDateTime expires,
			OffsetDateTime modified) {
		super();
		this.id = id;
		this.creation = creation;
		this.expires = expires;
		this.modified = modified;
	}

	public CommonEntity() {
		super();
		this.creation = OffsetDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public OffsetDateTime getCreation() {
		return creation;
	}

	public void setCreation(OffsetDateTime creation) {
		this.creation = creation;
	}

	public OffsetDateTime getExpires() {
		return expires;
	}

	public void setExpires(OffsetDateTime expires) {
		this.expires = expires;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(OffsetDateTime modified) {
		this.modified = modified;
	}

}
