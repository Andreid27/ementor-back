/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.service;

//SET HERE WHEN MODIFIED

public class ConstantUtils {
	public final static String RedisHashName = "StoredRedisToken";

	public final static long port = 49200;
	public final static String localEnvUrl = "http://localhost:" + port;

	public final static String productionHost = "http://api.ementor.dinca.one";

	public final static String productionService = "service1";
	public final static String productionEnvUrl = productionHost + "/" + productionService;
}
