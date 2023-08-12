/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.AddressSendDTO;
import com.ementor.profile.service.entity.Address;
import com.ementor.profile.service.entity.Location;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.AddressesRepo;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final LocationService locationService;

	private final AddressesRepo addressesRepo;

	public Address createAddress(AddressSendDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  address.", currentUserId);

		Location location = locationService.getLocation(dto.getCountyId());

		Address address = new Address(location, dto.getCity(), dto.getStreet(), dto.getNumber(), dto.getBlock(),
				dto.getStaircase(), dto.getApartment());

		return addressesRepo.save(address);
	}
}
