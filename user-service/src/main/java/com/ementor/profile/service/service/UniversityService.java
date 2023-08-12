/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.service;

import com.ementor.profile.service.core.exceptions.EmentorApiError;
import com.ementor.profile.service.core.service.SecurityService;
import com.ementor.profile.service.dto.UniversityDTO;
import com.ementor.profile.service.dto.UniversitySpecialitiesDTO;
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

	public List<UniversityDTO> getAll() {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting all universities.", currentUserId);

		List<University> universities = universitiesRepo.findAll();
		List<UniversityDTO> dtoList = new LinkedList<>();
		universities.forEach(speciality -> dtoList.add(buildUniversityDto(speciality)));

		log.info("[USER-ID: {}] Got all universities.", currentUserId);

		return dtoList;

	}
	public UniversityDTO get(UUID id) {
		UUID currentUserId = securityService.getCurrentUser()
			.getUserId();

		log.info("[USER-ID: {}] Getting university.", currentUserId);

		University university = getUniversity(id);

		log.info("[USER-ID: {}] Got university.", currentUserId);

		return buildUniversityDto(university);

	}

	private UniversityDTO buildUniversityDto(University university) {
		return UniversityDTO.builder()
			.id(university.getId())
			.name(university.getName())
			.address(addressService.buildAddressDto(university.getAddress()))
			.phone(university.getPhone())
			.examBook(university.getExamBook())
			.specialities(buildUniversitySpecialitiesDTO(university))
			.build();
	}

	private List<UniversitySpecialitiesDTO> buildUniversitySpecialitiesDTO(University university) {
		List<UniversitySpecialitiesDTO> universitySpecialitiesDTOS = new LinkedList<>();
		university.getSpecialities()
			.forEach(universitySpeciality -> universitySpecialitiesDTOS.add(UniversitySpecialitiesDTO.builder()
				.id(universitySpeciality.getId())
				.name(universitySpeciality.getSpeciality()
					.getName())
				.studyYears(universitySpeciality.getSpeciality()
					.getStudyYears())
				.specialityAbout(universitySpeciality.getSpeciality()
					.getAbout())
				.specialityId(universitySpeciality.getSpeciality()
					.getId())
				.difficulty(universitySpeciality.getDifficulty())
				.about(universitySpeciality.getAbout())
				.build()));
		return universitySpecialitiesDTOS;
	}

	public void createUniversity(UniversityDTO dto) {
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
			UniversityDTO dto) {
		university.setName(dto.getName());
		university.setAddress(addressService.createAddress(dto.getAddress()));
		university.setPhone(dto.getPhone());
		university.setExamBook(dto.getExamBook());
		return universitiesRepo.save(university);
	}

	private void saveUniversitySpecialities(University university,
			UniversityDTO dto) {
		List<UniversitySpeciality> universitySpecialityList = new LinkedList<>();
		dto.getSpecialities()
			.forEach(universitySpecialitiesSendDTO -> {
				Speciality speciality = specialityService.getSpeciality(universitySpecialitiesSendDTO.getSpecialityId());
				UniversitySpeciality universitySpeciality = new UniversitySpeciality(university, speciality,
						universitySpecialitiesSendDTO.getDifficulty(), universitySpecialitiesSendDTO.getAbout());
				universitySpecialityList.add(universitySpeciality);
			});

		universitySpecialitiesRepo.saveAll(universitySpecialityList);
	}



	public University getUniversity(final UUID universityId) {
		return universitiesRepo.findById(universityId)
			.orElseThrow(() -> new EmentorApiError("Speciality not found"));
	}
}
