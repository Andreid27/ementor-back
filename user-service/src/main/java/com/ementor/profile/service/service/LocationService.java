/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.LocationDTO;
import com.ementor.profile.service.entity.Location;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.LocationRepo;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
	private final LocationRepo repo;

	private final SecurityService securityService;

	public LocationDTO get(UUID id) {
		securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR, RoleEnum.STUDENT);
		return repo.findById(id)
			.map(l -> new LocationDTO(l.getId(), l.getName()))
			.orElseThrow(() -> new EmentorApiError("Could not identify location!", 404, id));
	}

	public List<LocationDTO> getAllByLevelCodes(List<String> codes) {
		return repo.findAllByCodes(codes)
			.stream()
			.map(l -> new LocationDTO(l.getId(), l.getName()))
			.toList();
	}

	public List<LocationDTO> getAllByCodes(List<String> codes) {
		return repo.findAllByCodes(codes)
			.stream()
			.map(l -> new LocationDTO(l.getId(), l.getName()))
			.toList();
	}

	public UUID getIdByCode(String codeLocation,
			List<String> codesLevel) {
		return repo.findByCode(codeLocation, codesLevel);
	}

	public Location findById(final UUID id) {
		return repo.findById(id)
			.orElseThrow(() -> new EmentorApiError("Could not identify location!", 404, id));
	}

	public Location getLocation(final UUID locationId) {
		return repo.findById(locationId)
			.orElseThrow(() -> new EmentorApiError("Location not found"));
	}
}
