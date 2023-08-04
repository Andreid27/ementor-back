/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.utils;

//SET HERE WHEN MODIFIED

public class ConstantUtils {
	public static final String RedisHashName = "StoredRedisToken";

	public static final long port = 49202;
	public static final String localEnvUrl = "http://localhost:" + port;

	public static final String productionHost = "http://api.ementor.dinca.one";

	public static final String productionService = "service1";
	public static final String productionEnvUrl = productionHost + "/" + productionService;
}
