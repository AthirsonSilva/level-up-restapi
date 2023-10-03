package com.api.nextspring.containers;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class BaseTest {

	@Container
	private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER;

	static {
		POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:16");
		POSTGRESQL_CONTAINER.withDatabaseName("next_spring_test");
		POSTGRESQL_CONTAINER.withUsername("test_username");
		POSTGRESQL_CONTAINER.withPassword("test_password");
		POSTGRESQL_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void setProperties(DynamicPropertyRegistry propertyRegistry) {
		propertyRegistry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
		propertyRegistry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
		propertyRegistry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
	}

}
