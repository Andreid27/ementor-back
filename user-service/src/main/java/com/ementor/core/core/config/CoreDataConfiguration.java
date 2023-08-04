/* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
package com.ementor.core.core.config;

import com.ementor.core.entity.User;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
	basePackages = "com.ementor.core.repo",
	entityManagerFactoryRef = "coreEntityManagerFactory",
	transactionManagerRef = "coreTransactionManager")
public class CoreDataConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties("ementor.datasource.core")
	public DataSourceProperties coreDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties("ementor.datasource.core.configuration")
	public DataSource coreDataSource(@Value("${ementor.migration.out-of-order}") Boolean outOfOrder) {
		final DataSourceProperties props = coreDataSourceProperties();
		FlywayUtils.migrateData(props, "classpath:db/core", outOfOrder);
		return props.initializeDataSourceBuilder()
			.type(HikariDataSource.class)
			.build();
	}

	@Bean(name = "coreEntityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean coreEntityManagerFactory(EntityManagerFactoryBuilder builder,
			DataSource dataSource) {
		return builder.dataSource(dataSource)
			.packages(User.class)
			.build();
	}

	@Bean(name = "coreTransactionManager")
	@Primary
	public PlatformTransactionManager coreTransactionManager(
			final @Qualifier("coreEntityManagerFactory") LocalContainerEntityManagerFactoryBean coreEntityManagerFactory) {
		return new JpaTransactionManager(Objects.requireNonNull(coreEntityManagerFactory.getObject()));
		// adaugat de sonarLint Object.requireNonNull -> vezi daca ai erori sa
		// il scoti
	}

}