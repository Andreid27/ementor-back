/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.core.entity.pagination.filter;

import com.ementor.quiz.core.exceptions.EmentorApiError;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FilterCriteriaUtils {
	private static final Logger log = LoggerFactory.getLogger(FilterCriteriaUtils.class);
	private static Map<Class<?>, Function<FilterCriteria<?>, FilterCriteria<?>>> classMapConverters = new HashMap();

	private FilterCriteriaUtils() {
		throw new AssertionError("Class " + FilterCriteriaUtils.class + " cannot be instantiated!");
	}

	public static FilterCriteria<?> convert(final FilterCriteria<?> filter,
			final Class<?> valueClass) {
		if (filter == null) {
			throw new EmentorApiError("Filter is null!");
		} else if (valueClass == null) {
			throw new EmentorApiError("Filter value class type is not specified!");
		} else if (!valueClass.isEnum()) {
			if (!classMapConverters.containsKey(valueClass)) {
				throw new EmentorApiError("Value not supported for field " + filter.getKey());
			} else {
				return (FilterCriteria) ((Function) classMapConverters.get(valueClass)).apply(filter);
			}
		} else {
			return (FilterCriteria) ((Function) classMapConverters.get(String.class)).apply(filter);
		}
	}

	public static <V extends Comparable<V>> FilterCriteria<V> convert(FilterCriteria<?> filter,
			Class<V> valueClass,
			V value,
			Collection<V> values) {
		FilterCriteria<V> newFilter = new FilterCriteria();
		newFilter.setKey(filter.getKey());
		newFilter.setOperation(filter.getOperation());
		newFilter.setValue(value);
		newFilter.setValues(values);
		newFilter.setValueClass(valueClass);
		return newFilter;
	}

	public static Date convertToDate(final String iso) {
		return Date.from(LocalDateTime.parse(iso)
			.toInstant(ZoneOffset.UTC));
	}

	public static LocalDate convertToLocalDate(final String iso) {
		return LocalDate.parse(iso);
	}

	public static LocalDateTime convertToLocalDateTime(final String iso) {
		return LocalDateTime.parse(iso);
	}

	static {
		classMapConverters.put(OffsetDateTime.class, (filter) -> {
			return convert(filter, OffsetDateTime.class, (OffsetDateTime) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return OffsetDateTime.parse(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return OffsetDateTime.parse(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(Date.class, (filter) -> {
			return convert(filter, Date.class, (Date) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return convertToDate(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return convertToDate(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(String.class, (filter) -> {
			return convert(filter, String.class, (String) Optional.ofNullable(filter.getValue())
				.map(Object::toString)
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map(Object::toString)
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(Integer.class, (filter) -> {
			return convert(filter, Integer.class, (Integer) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return Integer.parseInt(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return Integer.parseInt(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(Long.class, (filter) -> {
			return convert(filter, Long.class, (Long) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return Long.parseLong(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return Long.parseLong(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(BigDecimal.class, (filter) -> {
			return convert(filter, BigDecimal.class, (BigDecimal) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return new BigDecimal(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return new BigDecimal(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(Boolean.class, (filter) -> {
			return convert(filter, Boolean.class, (Boolean) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return Boolean.parseBoolean(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return Boolean.parseBoolean(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
		classMapConverters.put(UUID.class, (filter) -> {
			return convert(filter, UUID.class, (UUID) Optional.ofNullable(filter.getValue())
				.map((v) -> {
					return UUID.fromString(v.toString());
				})
				.orElseGet(() -> {
					return null;
				}), (Collection) Optional.ofNullable(filter.getValues())
					.map((vs) -> {
						return (List) vs.stream()
							.map((v) -> {
								return UUID.fromString(v.toString());
							})
							.collect(Collectors.toList());
					})
					.orElseGet(() -> {
						return null;
					}));
		});
	}
}