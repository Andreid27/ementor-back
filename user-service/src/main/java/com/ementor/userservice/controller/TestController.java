package com.ementor.userservice.controller;

import com.ementor.userservice.redis.entity.StoredRedisToken;
import com.ementor.userservice.redis.repo.StoredRedisTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class TestController {

    private final StoredRedisTokenRepo dao;

    @GetMapping("/tokens")
    public List<StoredRedisToken> motherToken() {
        // get all tokens from Redis
        Iterable<StoredRedisToken> tokens = dao.findAll();

        // convert iterable to list
        List<StoredRedisToken> tokenList = new ArrayList<>();
        tokens.forEach(tokenList::add);

        // return the list
        return tokenList;
    }
}
