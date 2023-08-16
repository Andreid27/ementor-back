/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.repo;

import com.ementor.userservice.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsersRepo extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

	User findByFirstName(String firstName);

	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);
}
