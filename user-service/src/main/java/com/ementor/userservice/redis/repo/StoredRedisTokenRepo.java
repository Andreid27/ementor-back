/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.redis.repo;

import com.ementor.userservice.redis.entity.StoredRedisToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface StoredRedisTokenRepo extends CrudRepository<StoredRedisToken, String> {
	List<StoredRedisToken> findAllValidTokenByUserId(UUID id);

	Optional<StoredRedisToken> findByToken(String token);

}