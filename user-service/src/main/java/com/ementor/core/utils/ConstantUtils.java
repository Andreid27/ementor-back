/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.utils;

//SET HERE WHEN MODIFIED

public class ConstantUtils {

	private ConstantUtils(){}

	public static final String REDIS_HASH_NAME = "StoredRedisToken";

	public static final String SERVICE_NAME = "profile.service";

	public static final long PORT = 49200;

	public static final String LOCAL_ENV_URL = "http://localhost:" + PORT;

	public static final String PRODUCTION_HOST = "http://api.ementor.dinca.one";

	public static final String PRODUCTION_SERVICE = "service0";

	public static final String PRODUCTION_ENV_URL = PRODUCTION_HOST + "/" + PRODUCTION_SERVICE;
}
