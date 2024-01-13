package com.api.levelup.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for registering an authorization interceptor to handle
 * requests to the "/api/v1/admin/**" endpoint.
 * 
 * @see WebMvcConfigurer
 * @see AuthorizationInterceptor
 * @see InterceptorRegistry
 * 
 * @author Athirson Silva
 */
@Configuration
@RequiredArgsConstructor
public class InterceptorRegistration implements WebMvcConfigurer {

	private final AuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/v1/admin/**");
	}
}
