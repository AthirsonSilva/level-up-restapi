package com.api.nextspring.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfiguration {

	@Bean
	@Description("This bean configures the OpenAPI specification version 3.0.3")
	OpenAPI customOpenAPI() {
		ArrayList<Server> servers = new ArrayList<Server>();

		servers.add(new Server().url("https://next-spring.up.railway.app"));
		servers.add(new Server().url("https://localhost:8080"));

		return new OpenAPI().servers(servers)
				.components(new Components()
						.addSecuritySchemes("JWT", new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")));
	}
}
