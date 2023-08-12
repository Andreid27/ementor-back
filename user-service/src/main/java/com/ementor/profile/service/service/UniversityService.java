/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.UniversitySendDTO;
import com.ementor.profile.service.entity.Speciality;
import com.ementor.profile.service.entity.University;
import com.ementor.profile.service.entity.UniversitySpeciality;
import com.ementor.profile.service.enums.RoleEnum;
import com.ementor.profile.service.repo.UniversitiesRepo;
import com.ementor.profile.service.repo.UniversitySpecialitiesRepo;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UniversityService {
	private final Logger log = LoggerFactory.getLogger(getClass());

	private final SecurityService securityService;

	private final AddressService addressService;

	private final SpecialityService specialityService;

	private final UniversitiesRepo universitiesRepo;

	private final UniversitySpecialitiesRepo universitySpecialitiesRepo;

	public void createUniversity(UniversitySendDTO dto) {
		securityService.hasAnyRole(RoleEnum.ADMIN);
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Creating  university.", currentUserId);
		University university = new University();

		university = saveUniversity(university, dto);
		saveUniversitySpecialities(university, dto);

		log.info("[USER-ID: {}] Created  university.", currentUserId);
	}

	private University saveUniversity(University university,
			UniversitySendDTO dto) {
		university.setName(dto.getName());
		university.setAddress(addressService.createAddress(dto.getAddress()));
		university.setPhone(dto.getPhone());
		university.setExamBook(dto.getExamBook());
		return universitiesRepo.save(university);
	}

	private void saveUniversitySpecialities(University university,
			UniversitySendDTO dto) {
		List<UniversitySpeciality> universitySpecialityList = new LinkedList<>();
		dto.getSpecialities()
			.forEach(universitySpecialitiesSendDTO -> {
				Speciality speciality = specialityService.getSpeciality(universitySpecialitiesSendDTO.getSpeciality());
				UniversitySpeciality universitySpeciality = new UniversitySpeciality(university, speciality,
						universitySpecialitiesSendDTO.getDifficulty(), universitySpecialitiesSendDTO.getAbout());
				universitySpecialityList.add(universitySpeciality);
			});

		universitySpecialitiesRepo.saveAll(universitySpecialityList);
	}
}
