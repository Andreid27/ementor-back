package com.ementor.userservice.redis.services;

import com.ementor.userservice.service.ConstantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisCleanUpService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredKeys() {
        // get all the keys from Redis
        Set<String> keys = stringRedisTemplate.keys("*");
        System.out.println(keys);
        // iterate over the keys and delete the ones that have expired ttl
        for (String key : keys) {
            Long ttl = stringRedisTemplate.getExpire(key);
            System.out.println(ttl);
        }
    }

    // Get the set operations from the template
    @Scheduled(fixedRate = 59000)
    public void removeExpiredMembers() {
        // Get the set operations from the template
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();

        // Get all the members of the set
        Set<String> members = setOps.members(ConstantUtils.RedisHashName);

        // Loop through each member
        for (String member : members) {
            // Check if the corresponding key exists
            if (!stringRedisTemplate.hasKey("StoredRedisToken:"+member)) {
                // Remove the member from the set
                System.out.print(member+", ");
                setOps.remove(ConstantUtils.RedisHashName, member);
            }
        }
    }
}
