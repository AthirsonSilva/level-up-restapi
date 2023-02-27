package com.api.nextspring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	/**
	 * @param http Security configuration
	 * @return SecurityFilterChain, which is used to configure the security of web requests
	 * @throws Exception if an error occurs
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeHttpRequests()
				.requestMatchers("/api/v1/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
				.anyRequest().authenticated();

		return http.build();
	}
}
