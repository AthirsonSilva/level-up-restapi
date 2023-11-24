package com.api.nextspring.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * This bean configures the OpenAPI specification version 3.0.3
 *
 * @see OpenAPI
 *
 * @return OpenAPI object with custom configuration
 *
 * @author Athirson Silva
 */
@Configuration
@Profile("dev")
public class SwaggerConfiguration {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	/**
	 * This method returns a custom OpenAPI specification version 3.0.3 object with
	 * configured servers, info, and security scheme.
	 * 
	 * @return OpenAPI object with configured servers, info, and security scheme.
	 */
	@Bean
	@Description("This bean configures the OpenAPI specification version 3.0.3")
	OpenAPI customOpenAPI() {
		OpenAPI openApi = new OpenAPI();

		ArrayList<Server> servers = new ArrayList<Server>();

		if (activeProfile.equals("prod"))
			servers.add(new Server().url("https://next-spring.up.railway.app").description("Production"));

		else
			servers.add(new Server().url("http://localhost:8080").description("Development"));

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
