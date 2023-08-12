/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.redis.services;

import com.ementor.profile.service.utils.ConstantUtils;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RedisCleanUpService {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Value("${ementor.config.redis.garbage-delete}")
	private final long cleanRedisSetGarbageValues = 24 * 60 * 60000; // every
																		// 24h
	@Scheduled(fixedRate = cleanRedisSetGarbageValues)
	public void removeExpiredMembers() {
		// Get the set operations from the template
		SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();

		// Get all the members of the set
		Set<String> members = setOps.members(ConstantUtils.REDIS_HASH_NAME);

		// Loop through each member
		for (String member : members) {
			// Check if the corresponding key exists
			if (!stringRedisTemplate.hasKey(ConstantUtils.REDIS_HASH_NAME + ":" + member)) {
				// Remove the member from the set
				setOps.remove(ConstantUtils.REDIS_HASH_NAME, member);
			}
		}
	}
}
