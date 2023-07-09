package com.ementor.userservice.redis.services;

import com.ementor.userservice.redis.entity.StoredRedisToken;
import com.ementor.userservice.redis.repo.StoredRedisTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoredRedisTokenService {

    private final StoredRedisTokenRepo repo;

    public void buildAndSaveToken(UserDetails userDetails, String token){
        StoredRedisToken storedRedisToken = StoredRedisToken.builder()
                .id(userDetails.getUsername())
                .token(token)
                .revoked(false)
                .userId(UUID.randomUUID()) //TODO "later to be added if needed"
                .build();
        saveStoredRedisToken(storedRedisToken);
    }

    private void saveStoredRedisToken(StoredRedisToken storedRedisToken) {
        try {
            repo.save(storedRedisToken);
        } catch (Exception e){
            System.out.println("Eroare la salvarea StoredRedisToken in Redis");
            throw e;
        }
    }


    public Optional<StoredRedisToken> getStoredRedisToken(String userName){
        return repo.findById(userName);
    }

}
