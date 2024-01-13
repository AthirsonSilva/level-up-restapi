package com.api.levelup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
public class AuthenticationManagerConfiguration {

	/**
	 * Returns the authentication manager bean.
	 *
	 * @param configuration configuration
	 * @return {@link AuthenticationManager}
	 * @throws Exception java.lang. exception
	 * @see AuthenticationManager
	 */
	@Bean
	AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
