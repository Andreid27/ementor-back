/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.SpecialityDTO;
import com.ementor.profile.service.entity.Speciality;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.SpecialitiesRepo;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialityService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final SpecialitiesRepo specialitiesRepo;

	public List<SpecialityDTO> getAll() {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting all specialities.", currentUserId);

		List<Speciality> specialities = specialitiesRepo.findAll();
		List<SpecialityDTO> dtoList = new LinkedList<>();
		specialities.forEach(speciality -> dtoList.add(buildSpecialityDto(speciality)));

		log.info("[USER-ID: {}] Got all specialities.", currentUserId);

		return dtoList;
	}
	public SpecialityDTO get(UUID specialityId) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting speciality with id {}.", currentUserId, specialityId);

		SpecialityDTO dto = buildSpecialityDto(getSpeciality(specialityId));

		log.info("[USER-ID: {}] Got speciality with id {}.", currentUserId, specialityId);

		return dto;
	}

	private SpecialityDTO buildSpecialityDto(Speciality speciality) {
		return SpecialityDTO.builder()
			.id(speciality.getId())
			.name(speciality.getName())
			.studyYears(speciality.getStudyYears())
			.about(speciality.getAbout())
			.build();
	}

	public void createSpeciality(SpecialityDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  speciality {}.", currentUserId, dto.getName());

		Speciality speciality = Speciality.builder()
			.name(dto.getName())
			.studyYears(dto.getStudyYears())
			.about(dto.getAbout())
			.build();

		specialitiesRepo.save(speciality);
		log.info("[USER-ID: {}] Created  speciality {} and id {}.", currentUserId, speciality.getName(), speciality.getId());
	}
	public void updateSpeciality(SpecialityDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Updating  speciality with id {}.", currentUserId, dto.getId());

		Speciality speciality = Speciality.builder()
			.name(dto.getName())
			.studyYears(dto.getStudyYears())
			.about(dto.getAbout())
			.build();
		speciality.setId(dto.getId());

		specialitiesRepo.save(speciality);
		log.info("[USER-ID: {}] Updated  speciality {} and id {}.", currentUserId, speciality.getName(), speciality.getId());
	}

	public void deleteSpeciality(UUID specialityId) {
		securityService.hasAnyRole(RoleEnum.ADMIN);

		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Deleting  speciality with id {}.", currentUserId, specialityId);

		Speciality speciality = getSpeciality(specialityId);
		specialitiesRepo.delete(speciality);

		log.info("[USER-ID: {}] Deleted  speciality with id {}.", currentUserId, specialityId);

	}

	public Speciality getSpeciality(final UUID locationId) {
		return specialitiesRepo.findById(locationId)
			.orElseThrow(() -> new EmentorApiError("Speciality not found"));
	}
}
