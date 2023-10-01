/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.repo;

import com.ementor.profile.service.entity.Address;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AddressesRepo extends JpaRepository<Address, UUID>, JpaSpecificationExecutor<Address> {
}
