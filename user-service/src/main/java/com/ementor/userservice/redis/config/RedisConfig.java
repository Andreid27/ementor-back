///* Copyright (C) 2022-2023 Ementor Romania - All Rights Reserved */
//package com.ementor.userservice.redis.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
//import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//@EnableRedisRepositories
//@ConfigurationProperties("spring.redis1")
//public class RedisConfig {
//
//	@Bean
//	public JedisConnectionFactory connectionFactory() {
//		RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//		configuration.setHostName("192.168.1.155");
//		configuration.setPort(6379);
//		return new JedisConnectionFactory(configuration);
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> template() {
//		RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(connectionFactory());
//		template.setKeySerializer(new StringRedisSerializer());
//		template.setHashKeySerializer(new StringRedisSerializer());
//		template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//		template.setValueSerializer(new JdkSerializationRedisSerializer());
//		template.setEnableTransactionSupport(true);
//		template.afterPropertiesSet();
//		return template;
//	}
//
//}