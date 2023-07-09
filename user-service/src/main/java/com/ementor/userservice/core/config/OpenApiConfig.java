/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.userservice.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import utils.ConstantUtils;

@OpenAPIDefinition(
	info = @Info(
		contact = @Contact(name = "Dinca Andrei", email = "andrei@dinca.one", url = "http://dinca.one/"),
		description = "OpenApi documentation for E-mentor app",
		title = "OpenApi specification - E-mentor App",
		version = "1.0",
		license = @License(name = "Toate drepturile rezervare @E-mentor app", url = "http://dinca.one/"),
		termsOfService = "Terms of service"),
	servers = {@Server(description = "Local ENV", url = ConstantUtils.localEnvUrl),
			@Server(description = "PROD ENV", url = ConstantUtils.productionEnvUrl)},
	security = {@SecurityRequirement(name = "Bearer Token Scheme - JWT Tokens")})
@SecurityScheme(
	name = "Bearer Token Scheme - JWT Tokens",
	description = "JWT stored tokens",
	scheme = "bearer",
	type = SecuritySchemeType.HTTP,
	bearerFormat = "JWT",
	in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}