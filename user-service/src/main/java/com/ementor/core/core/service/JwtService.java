/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.service;

import com.ementor.core.core.redis.entity.StoredRedisToken;
import com.ementor.core.core.redis.services.StoredRedisTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
	@Value("${ementor.security.jwt.secret-key}")
	private String secretKey;
	@Value("${ementor.security.jwt.expiration}")
	private long jwtExpiration;
	@Value("${ementor.security.jwt.refresh-token.expiration}")
	private long refreshExpiration;

	private final StoredRedisTokenService storedRedisTokenService;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token,
			Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public boolean isTokenValid(String token,
			UserDetails userDetails) {
		Date tokenDate = extractExpiration(token);
		boolean isTokenExpired = isTokenExpired(tokenDate);
		boolean isTokenLastest = isTokenLastest(userDetails, token, tokenDate);
		return isTokenLastest && !isTokenExpired;

	}

	private boolean isTokenExpired(Date tokenDate) {
		return tokenDate.before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSignInKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean isTokenLastest(UserDetails userDetails,
			String token,
			Date tokenDate) {
		Optional<StoredRedisToken> storedRedisToken = storedRedisTokenService.getStoredRedisToken(userDetails.getUsername());
		if (storedRedisToken.isEmpty()) {
			storedRedisTokenService.buildAndSaveToken(userDetails, token);
			return true;
		}

		if (storedRedisToken.get()
			.getToken()
			.equals(token)) {
			return true;
		}

		Date storedTokenDate = extractExpiration(storedRedisToken.get()
			.getToken());
		if (storedTokenDate.before(tokenDate)) {
			storedRedisTokenService.buildAndSaveToken(userDetails, token);
			return true;
		}

		return false;
	}
}
