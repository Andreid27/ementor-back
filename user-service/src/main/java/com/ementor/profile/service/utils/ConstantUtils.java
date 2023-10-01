/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.utils;

//SET HERE WHEN MODIFIED

public class ConstantUtils {
	private ConstantUtils() {
	}
	public static final String REDIS_HASH_NAME = "StoredRedisToken";

	// what is after com.ementor.
	public static final String SERVICE_NAME = "profile.service";

	public static final long PORT = 49202;

	public static final String LOCAL_ENV_URL = "http://localhost:" + PORT;

	public static final String PRODUCTION_HOST = "https://api.e-mentor.ro";

	public static final String PRODUCTION_SERVICE = "service2";

	public static final String PRODUCTION_ENV_URL = PRODUCTION_HOST + "/" + PRODUCTION_SERVICE;

	public static final String USER_SERVICE_PROD_URL = "http://api.e-mentor.ro" + "/service1";
}
