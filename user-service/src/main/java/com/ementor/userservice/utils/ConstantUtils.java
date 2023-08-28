/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.utils;

//SET HERE WHEN MODIFIED

public class ConstantUtils {

	private ConstantUtils() {
	}

	public static final String STORED_REDIS_TOKEN = "StoredRedisToken";

	public static final long PORT = 49201;
	public static final String LOCAL_ENV_URL = "http://localhost:" + PORT;

	public static final String PRODUCTION_HOST = "http://api.e-mentor.ro";

	public static final String PRODUCTION_SERVICE = "service1";
	public static final String PRODUCTION_ENV_URL = PRODUCTION_HOST + "/" + PRODUCTION_SERVICE;
}
