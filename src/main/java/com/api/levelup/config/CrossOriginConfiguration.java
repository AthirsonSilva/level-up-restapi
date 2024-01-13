package com.api.levelup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CrossOriginConfiguration {

	/**
	 * Returns a CorsConfigurationSource object that applies default CORS
	 * configuration values to all endpoints.
	 * 
	 * @return the CorsConfigurationSource object
	 */
	@Bean
	CorsConfigurationSource configurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",
				new CorsConfiguration().applyPermitDefaultValues());

		return source;
	}

}
