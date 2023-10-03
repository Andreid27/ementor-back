/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.quiz.utils;

//SET HERE WHEN MODIFIED

public class ConstantUtils {
	private ConstantUtils() {
	}
	public static final String REDIS_HASH_NAME = "StoredRedisToken";

	// what is after com.ementor.
	public static final String SERVICE_NAME = "quiz";

	public static final long PORT = 49203;

	public static final String LOCAL_ENV_URL = "http://localhost:" + PORT;

	public static final String PRODUCTION_HOST = "https://api.e-mentor.ro";

	public static final String PRODUCTION_SERVICE = "service3";

	public static final String PRODUCTION_ENV_URL = PRODUCTION_HOST + "/" + PRODUCTION_SERVICE;

	public static final String USER_SERVICE_PROD_URL = PRODUCTION_HOST + "/service3";
}
