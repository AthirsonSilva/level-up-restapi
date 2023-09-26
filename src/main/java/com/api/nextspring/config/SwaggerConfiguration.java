package com.api.nextspring.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfiguration {

	@Bean
	@Description("This bean configures the OpenAPI specification version 3.0.3")
	OpenAPI customOpenAPI() {
		OpenAPI openApi = new OpenAPI();

		ArrayList<Server> servers = new ArrayList<Server>();

		servers.add(new Server().url("https://next-spring.up.railway.app").description("Production"));
		servers.add(new Server().url("http://localhost:8080").description("Development:8080"));
		servers.add(new Server().url("http://localhost:8000").description("Development:8000"));

		openApi.setServers(servers);

		Info info = new Info()
				.title("Next Spring API")
				.description("API for Next Spring")
				.contact(new Contact().name("Athirson Silva").email("athirsonarceus@gmail.com")
						.url("https://athirsonsilva.github.io/personal-portfolio/"))
				.version("2.0.0")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
				.termsOfService("https://www.apache.org/licenses/LICENSE-2.0");

		openApi.setInfo(info);

		SecurityScheme securityScheme = new SecurityScheme()
				.name("Authorization")
				.description("JWT Token")
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER);

		openApi.components(new Components().addSecuritySchemes("JWT Authentication", securityScheme));

		return openApi;
	}

}
