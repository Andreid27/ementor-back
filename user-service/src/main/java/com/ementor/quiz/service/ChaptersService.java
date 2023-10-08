package com.ementor.quiz.service;

import com.ementor.quiz.core.entity.pagination.*;
import com.ementor.quiz.core.entity.pagination.filter.FilterGroup;
import com.ementor.quiz.core.entity.pagination.filter.FilterOption;
import com.ementor.quiz.core.entity.pagination.filter.FilterOptionUtils;
import com.ementor.quiz.core.entity.pagination.filter.FilterType;
import com.ementor.quiz.core.exceptions.EmentorApiError;
import com.ementor.quiz.core.service.SecurityService;
import com.ementor.quiz.dto.ChapterDTO;
import com.ementor.quiz.entity.Chapter;
import com.ementor.quiz.entity.User;
import com.ementor.quiz.enums.RoleEnum;
import com.ementor.quiz.repo.ChaptersRepo;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChaptersService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EntityManager entityManager;

    private final SecurityService securityService;

    private final ChaptersRepo chaptersRepo;

    public PaginatedResponse<Chapter> getPaginated(PaginatedRequest request) {
        securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
        User user = securityService.getCurrentUser();

        log.info("[USER-ID:{}] Getting chapters list - page {}", user.getUserId(), request.getPage());


        final PaginatedRequestSpecification<Chapter> spec = PaginatedRequestSpecificationUtils
                .genericSpecification(request.bind(Chapter.class), false, Chapter.class);
        Page<Chapter> findAll = chaptersRepo.findAll(spec, ServiceUtils.convertToPageRequest(request));
        PaginatedResponse<Chapter> paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setData(findAll.getContent()
                .stream()
                .toList());
        ServiceUtils.extractPaginationMetadata(findAll, paginatedResponse);
        paginatedResponse.setFilterOptions(filterOptions(request));
        paginatedResponse.setCurrentRequest(request);

        log.info("[USER-ID:{}] Got questions list - page {}", user.getUserId(), request.getPage());

        return paginatedResponse;
    }

    public List<FilterOption<?>> filterOptions(PaginatedRequest request) {
        return FilterOptionUtils.createFilterOptions(entityManager, request, Chapter.class,
                new FilterGroup("title", FilterType.TEXT_OPTIONS),
                new FilterGroup("description", FilterType.TEXT_CONTENT));
    }

    public ChapterDTO getChapter(UUID chapterId) {
        securityService.hasAnyRole(RoleEnum.PROFESSOR, RoleEnum.STUDENT);

        UUID currentUserId = securityService.getCurrentUser()
                .getUserId();

        log.info("[USER-ID: {}] Getting  chapter.", currentUserId);

        Chapter chapter = getChapterById(chapterId);

        log.info("[USER-ID: {}] Got  chapter.", currentUserId);

        return ChapterDTO.builder()
                .id(chapter.getId())
                .title(chapter.getTitle())
                .description(chapter.getDescription())
                .createdBy(chapter.getCreatedBy())
                .build();
    }

    @Transactional
    public void create(ChapterDTO dto) {
        securityService.hasAnyRole(RoleEnum.ADMIN, RoleEnum.PROFESSOR);
        UUID currentUserId = securityService.getCurrentUser()
                .getUserId();

        log.info("[USER-ID: {}] Creating  chapter.", currentUserId);

        Chapter chapter = saveChapter(dto);
        chapter.setCreatedBy(currentUserId);

        log.info("[USER-ID: {}] Created  chapter.", currentUserId);

        chaptersRepo.save(chapter);
    }

    private Chapter saveChapter(ChapterDTO dto) {
        return Chapter.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .build();
    }

    @Transactional
    public void update(ChapterDTO dto) {
        securityService.hasAnyRole(RoleEnum.PROFESSOR);
        UUID currentUserId = securityService.getCurrentUser()
                .getUserId();

        log.info("[USER-ID: {}] Updating  chapter.", currentUserId);

        Chapter oldChapter = getChapterById(dto.getId());
        Chapter chapter = saveChapter(dto);
        chapter.setId(dto.getId());
        chapter.setCreatedBy(oldChapter.getCreatedBy());

        log.info("[USER-ID: {}] Updated  chapter.", currentUserId);


        chaptersRepo.save(chapter);
    }

    public void delete(UUID chapterId) {
        securityService.hasAnyRole(RoleEnum.PROFESSOR);

        UUID currentUserId = securityService.getCurrentUser()
                .getUserId();

        log.info("[USER-ID: {}] Deleting  chapter with id {}.", currentUserId, chapterId);

        Chapter chapter = getChapterById(chapterId);
        chapter.setExpires(OffsetDateTime.now());
        chaptersRepo.save(chapter);

        log.info("[USER-ID: {}] Deleted  chapter with id {}.", currentUserId, chapterId);
    }

    private Chapter getChapterById(UUID chapterId) {
        return chaptersRepo.findById(chapterId)
                .orElseThrow(() -> new EmentorApiError("Chapter not found", 404));
    }
}
