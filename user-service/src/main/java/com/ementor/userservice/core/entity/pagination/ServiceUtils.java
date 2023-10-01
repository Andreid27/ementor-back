/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.entity.pagination;

import com.ementor.userservice.core.entity.pagination.filter.SortCriteria;
import com.ementor.userservice.core.entity.pagination.filter.SortDirection;
import com.ementor.userservice.core.exceptions.EmentorApiError;
import com.google.javascript.jscomp.jarjar.com.google.re2j.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public final class ServiceUtils {

	public static <T> T checkRequired(final T value,
			final String errorMessage) {
		return checkRequiredOptional(value).orElseThrow(() -> new EmentorApiError(errorMessage, 400));
	}

	public static String checkRegex(final String value,
			final String regex,
			final String errorMessage) {
		if (!Pattern.matches(regex, value)) {
			throw new EmentorApiError(errorMessage, 400);
		}
		return value;
	}

	public static <T> Optional<T> checkRequiredOptional(final T value) {
		return Optional.ofNullable(value);
	}

	public static <T> T checkRequired(final Optional<T> value,
			final String errorMessage) {
		return value.orElseThrow(() -> new EmentorApiError(errorMessage, 400));
	}

	public static PageRequest convertToPageRequest(final PaginatedRequest request) {
		List<SortCriteria> sorters = Optional.ofNullable(request.getSorters())
			.orElse(new ArrayList<>());
		return PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(sorters.stream()
			.map(s -> SortDirection.ASC.equals(s.getDirection()) ? Order.asc(s.getKey()) : Order.desc(s.getKey()))
			.collect(Collectors.toList())));
	}

	public static void extractPaginationMetadata(Page<?> findAll,
			PaginatedResponse<?> paginatedRequest) {
		paginatedRequest.setPage(findAll.getNumber());
		paginatedRequest.setTotalCount(findAll.getTotalElements());
		paginatedRequest.setTotalPages(findAll.getTotalPages());
	}

	public static String getTemporaryFolder() {
		final String folderPath = System.getProperty("user.home") + File.separator + ".generated";
		final File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		return folderPath;
	}

	public static FileOutputStream createGeneratedFileOutputStream(final UUID fileId) throws FileNotFoundException {
		return new FileOutputStream(
				new File(ServiceUtils.getTemporaryFolder() + File.separator + fileId.toString() + ".xlsx"));
	}

	public static FileInputStream createGeneratedFileInputStream(final UUID fileId) throws FileNotFoundException {
		return new FileInputStream(
				new File(ServiceUtils.getTemporaryFolder() + File.separator + fileId.toString() + ".xlsx"));
	}
}