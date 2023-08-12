/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.AddressDTO;
import com.ementor.profile.service.entity.Address;
import com.ementor.profile.service.entity.Location;
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

	public Address createAddress(AddressDTO dto) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  address.", currentUserId);

		Location location = locationService.getLocation(dto.getCountyId());

		Address address = new Address(location, dto.getCity(), dto.getStreet(), dto.getNumber(), dto.getBlock(),
				dto.getStaircase(), dto.getApartment(), currentUserId);

		log.info("[USER-ID: {}] Created  address.", currentUserId);

		return addressesRepo.save(address);
	}

	public Address updateAddress(AddressDTO dto) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Updating  address.", currentUserId);

		Location county = locationService.getLocation(dto.getCountyId());
		Address address = getAddress(dto.getId());
		address.setCounty(county);
		address.setCity(dto.getCity());
		address.setStreet(dto.getStreet());
		address.setNumber(dto.getNumber());
		address.setBlock(dto.getBlock());
		address.setStaircase(dto.getStaircase());
		address.setApartment(dto.getApartment());

		log.info("[USER-ID: {}] Updated  address.", currentUserId);

		return addressesRepo.save(address);
	}

	public void deleteAddress(UUID addressId) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Deleting  address.", currentUserId);

		addressesRepo.deleteById(addressId);

		log.info("[USER-ID: {}] Deleted  address.", currentUserId);
	}

	public AddressDTO buildAddressDto(Address address) {
		return AddressDTO.builder()
			.id(address.getId())
			.countyId(address.getCounty()
				.getId())
			.countyValue(address.getCounty()
				.getName())
			.city(address.getCity())
			.street(address.getStreet())
			.number(address.getNumber())
			.block(address.getBlock())
			.staircase(address.getStaircase())
			.apartment(address.getApartment())
			.build();
	}

	public Address getAddress(UUID addressId) {
		return addressesRepo.findById(addressId)
			.orElseThrow(() -> new EmentorApiError("Address not found"));
	}
}
