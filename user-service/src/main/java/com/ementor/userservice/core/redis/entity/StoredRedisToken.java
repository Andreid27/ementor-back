/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.redis.entity;

import com.ementor.userservice.utils.ConstantUtils;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = ConstantUtils.RedisHashName, timeToLive = 7200L)
public class StoredRedisToken implements Serializable {
	@Id
	private String id; // in our case the id is the email

	private UUID userId;

	private String token;

	private Boolean revoked;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Boolean isRevoked() {
		return revoked;
	}

	public void setRevoked(Boolean revoked) {
		this.revoked = revoked;
	}
}
