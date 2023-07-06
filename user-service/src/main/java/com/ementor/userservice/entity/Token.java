/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.entity;

import com.ementor.userservice.enums.TokenTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

	@Id
	@GeneratedValue
	public Integer id;

	@Column(unique = true)
	public String token;

	@Enumerated(EnumType.STRING)
	public TokenTypeEnum tokenType = TokenTypeEnum.BEARER;

	public boolean revoked;

	public boolean expired;

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User user;
}
