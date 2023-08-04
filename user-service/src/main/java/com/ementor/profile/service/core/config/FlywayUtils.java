/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.profile.service.core.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public final class FlywayUtils {

	private static final Logger log = LoggerFactory.getLogger(FlywayUtils.class);

	private FlywayUtils() {

	}

	public static void migrateData(final DataSourceProperties props,
			String folder) {
		migrateData(props, folder, false);
	}

	public static void migrateData(final DataSourceProperties props,
			String folder,
			Boolean outOfOrder) {
		createLogger();
		Flyway.configure()
			.outOfOrder(outOfOrder) // inainte de sonarLint:
									// .outOfOrder(outOfOrder != null ?
									// outOfOrder : false)
			.locations(folder)
			.dataSource(props.getUrl(), props.getUsername(), props.getPassword())
			.load()
			.migrate();
	}

	private static void createLogger() {
		// modificat de sonarLint din LogFactory.setLogCreator(new LogCreator()
		// { -> in chestia aia cu lambda

		LogFactory.setLogCreator(clazz -> new Log() {

			@Override
			public void warn(String message) {
				log.warn(message);
			}

			@Override
			public boolean isDebugEnabled() {
				return true;
			}

			@Override
			public void info(String message) {
				log.info(message);
			}

			@Override
			public void error(String message,
					Exception e) {
				log.error(message, e);
			}

			@Override
			public void error(String message) {
				log.error(message);
			}

			@Override
			public void debug(String message) {
				log.debug(message);
			}

			@Override
			public void notice(String message) {
				log.info("[notice]{}", message);
			}
		});
	}
}
