/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.controller;

import com.ementor.userservice.dto.AuthenticationRequest;
import com.ementor.userservice.dto.AuthenticationResponse;
import com.ementor.userservice.dto.RegisterRequest;
import com.ementor.userservice.redis.entity.StoredRedisToken;
import com.ementor.userservice.redis.repo.StoredRedisTokenDao;
import com.ementor.userservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;

	private final StoredRedisTokenDao dao;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(service.register(request));
	}
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public void refreshToken(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		service.refreshToken(request, response);
	}

	@PostMapping("/token")
	public void token(@RequestBody RegisterRequest request) {
		StoredRedisToken storedRedisToken = new StoredRedisToken();
		storedRedisToken.setToken("wowowo motherfucker");
		storedRedisToken.setId(11L);
		dao.save(storedRedisToken);
	}


	@GetMapping("/token")
	public List<StoredRedisToken>  getToken() {
		// get all tokens from Redis
		Iterable<StoredRedisToken> tokens = dao.findAll();

		// convert iterable to list
		List<StoredRedisToken> tokenList = new ArrayList<>();
		tokens.forEach(tokenList::add);

		// return the list
		return tokenList;
	}


}
