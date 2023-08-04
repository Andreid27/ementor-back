/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.service;

import com.ementor.userservice.core.redis.entity.StoredRedisToken;
import com.ementor.userservice.core.redis.services.StoredRedisTokenService;
import com.ementor.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

	public String generateToken(User user) {
		HashMap<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("ROLE", user.getRole());
		extraClaims.put("userId", user.getId());
		String token = generateToken(extraClaims, user);
		storedRedisTokenService.buildAndSaveToken(user, token);
		return token;
	}

	public String generateToken(Map<String, Object> extraClaims,
			UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration);
	}

	private String buildToken(Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration) {
		// TODO pe aici se adauga ip-ul daca vrem
		return Jwts.builder()
			.setClaims(extraClaims)
			.setSubject(userDetails.getUsername())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + expiration))
			.signWith(getSignInKey(), SignatureAlgorithm.HS512)
			.compact();
	}

	public boolean isTokenValid(String token,
			UserDetails userDetails) {
		Date tokenDate = extractExpiration(token);
		final String username = extractUsername(token);
		boolean isUserNameSame = username.equals(userDetails.getUsername());
		boolean isTokenExpired = isTokenExpired(tokenDate);
		boolean isTokenLastest = isTokenLastest(userDetails, token, tokenDate);
		// TODO here add the token validation from the redis db
		return isTokenLastest && isUserNameSame && !isTokenExpired;

		// TODO de testat toata logica de autentificare cu jwt si de salvare a
		// noului token

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
