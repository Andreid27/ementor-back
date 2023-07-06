/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.redis.repo;

import com.ementor.userservice.redis.entity.StoredRedisToken;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Repository
//public class StoredRedisTokenDao {
//	@Autowired
//	private RedisTemplate template;
//
//	private static final String HASH_KEY = "StoredRedisToken";
//
//	public StoredRedisToken save(StoredRedisToken storedRedisToken) {
//		template.opsForHash()
//			.put(HASH_KEY, storedRedisToken.getId(), storedRedisToken);
//		return storedRedisToken;
//		// TODO modify to return void - just remove the return(is the same with
//		// the input)
//	}
//
//	public List<StoredRedisToken> findAll() {
//		return template.opsForHash()
//			.values(HASH_KEY);
//	}
//
//	public StoredRedisToken findStoredRedisTokenById(long id) {
//		return (StoredRedisToken) template.opsForHash()
//			.get(HASH_KEY, id);
//	}
//
//	public String deleteStoredRedisToken(long id) {
//		template.opsForHash()
//			.delete(HASH_KEY, id);
//		return "STORED REDIS KEY REMOVED!!!!";
//	}
//}
public interface StoredRedisTokenDao extends CrudRepository<StoredRedisToken, String> {
}