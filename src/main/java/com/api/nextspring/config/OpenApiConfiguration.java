package com.api.nextspring.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfiguration {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().servers(
				List.of(new Server().url("https://next-spring.up.railway.app"), new Server().url("http://localhost:8080")))
				.components(new Components()
						.addSecuritySchemes("xpto", new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}
